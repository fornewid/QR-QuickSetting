package soup.qr.ui.detect

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
import soup.qr.databinding.FragmentDetectBinding
import soup.qr.core.detector.QrCodeDetector
import soup.qr.core.detector.firebase.FirebaseQrCodeDetector
import soup.qr.model.QrCode

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
                startCameraWith()
            }
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
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

    private fun FragmentDetectBinding.startCameraWith() {
        val textureView: TextureView = cameraPreview
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

        val detector = FirebaseQrCodeDetector().apply {
            setCallback(object : QrCodeDetector.Callback {

                override fun onIdle() {
                    hintAnimation?.onIdle()
                }

                override fun onDetected(qrCode: QrCode) {
                    hintAnimation?.onSuccess()
                    findNavController().navigate(
                        QrDetectFragmentDirections.actionToDetail(
                            qrCode = qrCode
                        )
                    )
                }

                override fun onDetectFailed() {
                    hintAnimation?.onError()
                }
            })
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
                analyzer = QrImageAnalyzer(detector)
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
                binding.root.post {
                    binding.startCameraWith()
                }
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

    companion object {

        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
