package soup.qr.detect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Rational
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import soup.qr.databinding.FragmentDetectBinding
import java.util.concurrent.TimeUnit

class QrDetectFragment : Fragment() {

    private lateinit var binding: FragmentDetectBinding

    private var hintAnimation: QrDetectHintAnimation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initViewState()
        return binding.root
    }

    private fun FragmentDetectBinding.initViewState() {
        hintAnimation = QrDetectHintAnimation(this)

        val preview = cameraPreview
        if (allPermissionsGranted(root.context)) {
            preview.post {
                startCameraWith(preview)
            }
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        preview.doOnNextLayout {
            preview.updateTransform()
        }
    }

    override fun onResume() {
        super.onResume()
        hintAnimation?.start()
    }

    override fun onPause() {
        super.onPause()
        hintAnimation?.stop()
    }

    private fun startCameraWith(textureView: TextureView) {
        val metrics = DisplayMetrics().also { textureView.display.getRealMetrics(it) }
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val previewConfig = PreviewConfig.Builder()
            .apply {
                setTargetAspectRatio(screenAspectRatio)
                setTargetRotation(textureView.display.rotation)
            }
            .build()
        val preview = Preview(previewConfig)
        preview.onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener {
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)

            textureView.surfaceTexture = it.surfaceTexture
            textureView.updateTransform()
        }

        val recognizer = QrCodeDetector {
            findNavController().navigate(QrDetectFragmentDirections.actionToDetail(it))
        }
        val analyzerConfig = ImageAnalysisConfig.Builder()
            .apply {
                val analyzerThread = HandlerThread("QrCodeAnalysis").apply { start() }
                setCallbackHandler(Handler(analyzerThread.looper))
                setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                setTargetRotation(textureView.display.rotation)
            }
            .build()
        val analyzerUseCase = ImageAnalysis(analyzerConfig)
            .apply {
                analyzer = recognizer
            }

        CameraX.bindToLifecycle(viewLifecycleOwner, preview, analyzerUseCase)
    }

    private fun TextureView.updateTransform() {
        val matrix = Matrix()
        val centerX = width / 2f
        val centerY = height / 2f
        val rotationDegrees = when (display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        setTransform(matrix)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val context: Context = binding.root.context
            if (allPermissionsGranted(context)) {
                binding.root.post { startCameraWith(binding.cameraPreview) }
            } else {
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    private fun allPermissionsGranted(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private class QrCodeDetector(
        private val analyzeMillis: Long = 1000L,
        private val callback: (String) -> Unit
    ) : ImageAnalysis.Analyzer {

        private var lastAnalyzedTimestamp = 0L

        private val detector: FirebaseVisionBarcodeDetector

        init {
            val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build()
            detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        }

        override fun analyze(image: ImageProxy, rotationDegrees: Int) {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastAnalyzedTimestamp >= TimeUnit.MILLISECONDS.toMillis(
                    analyzeMillis
                )
            ) {
                lastAnalyzedTimestamp = currentTimestamp
                tryToRecognize(image, rotationDegrees)
            }
        }

        private fun tryToRecognize(image: ImageProxy, rotationDegrees: Int) {
            val y = image.planes[0]
            val u = image.planes[1]
            val v = image.planes[2]
            val Yb = y.buffer.remaining()
            val Ub = u.buffer.remaining()
            val Vb = v.buffer.remaining()
            val data = ByteArray(Yb + Ub + Vb)
            y.buffer.get(data, 0, Yb)
            u.buffer.get(data, Yb, Ub)
            v.buffer.get(data, Yb + Ub, Vb)

            val metadata = FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_YV12)
                .setHeight(image.height)
                .setWidth(image.width)
                .setRotation(getRotationFrom(rotationDegrees))
                .build()
            val labelImage = FirebaseVisionImage.fromByteArray(data, metadata)
            detector.detectInImage(labelImage)
                .addOnSuccessListener {
                    if (it.isNotEmpty()) {
                        callback(it.first().displayValue.orEmpty())
                    }
                }
        }

        private fun getRotationFrom(rotationDegrees: Int): Int {
            return when (rotationDegrees) {
                0 -> FirebaseVisionImageMetadata.ROTATION_0
                90 -> FirebaseVisionImageMetadata.ROTATION_90
                180 -> FirebaseVisionImageMetadata.ROTATION_180
                270 -> FirebaseVisionImageMetadata.ROTATION_270
                else -> FirebaseVisionImageMetadata.ROTATION_0
            }
        }
    }

    companion object {

        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
