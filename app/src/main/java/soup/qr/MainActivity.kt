package soup.qr

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

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
        val previewConfig = PreviewConfig.Builder()
            .apply {
                setTargetAspectRatio(Rational(1, 1))
                setTargetResolution(Size(640, 640))
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

        val recognizer = QrCodeRecognizer()

        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setTargetAspectRatio(Rational(1, 1))
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            }
            .build()
        val imageCapture = ImageCapture(imageCaptureConfig)
        findViewById<View>(R.id.captureButton).setOnClickListener {
            imageCapture.takePicture(recognizer)
        }

        val analyzerConfig = ImageAnalysisConfig.Builder()
            .apply {
                val analyzerThread = HandlerThread("LuminosityAnalysis").apply { start() }
                setCallbackHandler(Handler(analyzerThread.looper))
                setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            }
            .build()
        val analyzerUseCase = ImageAnalysis(analyzerConfig)
            .apply {
                analyzer = recognizer
            }

        CameraX.bindToLifecycle(this, preview, imageCapture, analyzerUseCase)
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
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private class QrCodeRecognizer : ImageCapture.OnImageCapturedListener(), ImageAnalysis.Analyzer {

        private var lastAnalyzedTimestamp = 0L

        override fun onCaptureSuccess(image: ImageProxy, rotationDegrees: Int) {
            analyze(image, rotationDegrees)
            super.onCaptureSuccess(image, rotationDegrees)
        }

        override fun analyze(image: ImageProxy, rotationDegrees: Int) {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastAnalyzedTimestamp >= TimeUnit.SECONDS.toMillis(1)) {
                tryToRecognize(image)
                lastAnalyzedTimestamp = currentTimestamp
            }
        }

        private fun tryToRecognize(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            //TODO: try to recognize
        }

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()
            val data = ByteArray(remaining())
            get(data)
            return data
        }
    }

    companion object {

        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
