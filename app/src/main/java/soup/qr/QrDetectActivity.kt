package soup.qr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Rational
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.util.concurrent.TimeUnit

class QrDetectActivity : AppCompatActivity() {

    private lateinit var viewFinder: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewFinder = findViewById(R.id.cameraPreview)

        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        viewFinder.doOnNextLayout {
            updateTransform()
        }
    }

    private fun startCamera() {
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val previewConfig = PreviewConfig.Builder()
            .apply {
                setTargetAspectRatio(screenAspectRatio)
                setTargetRotation(viewFinder.display.rotation)
            }
            .build()
        val preview = Preview(previewConfig)
        preview.onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener {
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        val recognizer = QrCodeDetector(this)
        val analyzerConfig = ImageAnalysisConfig.Builder()
            .apply {
                val analyzerThread = HandlerThread("QrCodeAnalysis").apply { start() }
                setCallbackHandler(Handler(analyzerThread.looper))
                setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                setTargetRotation(viewFinder.display.rotation)
            }
            .build()
        val analyzerUseCase = ImageAnalysis(analyzerConfig)
            .apply {
                analyzer = recognizer
            }

        CameraX.bindToLifecycle(this, preview, analyzerUseCase)
    }

    private fun updateTransform() {
        val matrix = Matrix()
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        viewFinder.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private class QrCodeDetector(
        private val context: Context,
        private val analyzeMillis: Long = 1000L
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
            if (currentTimestamp - lastAnalyzedTimestamp >= TimeUnit.MILLISECONDS.toMillis(analyzeMillis)) {
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
                        context.startActivity(Intent(context, QrResultActivity::class.java))
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
