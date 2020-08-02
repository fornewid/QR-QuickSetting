package soup.qr.ui.detect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import soup.qr.R
import soup.qr.core.detector.BarcodeDetector
import soup.qr.core.detector.firebase.MLKitBarcodeDetector
import soup.qr.databinding.DetectBinding
import soup.qr.ui.EventObserver
import soup.qr.ui.detect.BarcodeDetectFragmentDirections.Companion.actionToResult
import soup.qr.ui.setOnDebounceClickListener
import soup.qr.util.Gallery
import soup.qr.util.ScopedExecutor
import soup.qr.util.toBitmap
import soup.qr.util.toast
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeDetectFragment : Fragment(R.layout.detect) {

    private val viewModel: BarcodeDetectViewModel by viewModels()

    private var binding: DetectBinding? = null
    private var hintAnimation: BarcodeDetectHintAnimation? = null

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var scopedExecutor: ScopedExecutor
    private var barcodeDetector: BarcodeDetector? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newCachedThreadPool()
        scopedExecutor = ScopedExecutor(cameraExecutor)
        barcodeDetector = MLKitBarcodeDetector(
            viewLifecycleOwner,
            cameraExecutor,
            onDetected = { viewModel.onDetected(it) }
        )

        with(DetectBinding.bind(view)) {
            hintAnimation = BarcodeDetectHintAnimation(this)

            pickImageButton.setOnDebounceClickListener {
                requestPermissions(STORAGE_PERMISSION, REQUEST_CODE_STORAGE)
            }

            viewModel.showResultEvent.observe(viewLifecycleOwner, EventObserver {
                findNavController().navigate(actionToResult(barcode = it))
            })

            binding = this

            if (view.context.checkPermissionsGranted(CAMERA_PERMISSION)) {
                bindCameraUseCases()
            } else {
                requestPermissions(CAMERA_PERMISSION, REQUEST_CODE_CAMERA)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        scopedExecutor.shutdown()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun DetectBinding.bindCameraUseCases() = viewFinder.post {
        val context = root.context
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(viewFinder.display.rotation)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(
                scopedExecutor, ImageAnalysis.Analyzer { proxy ->
                    proxy.use {
                        val bitmap = proxy.image?.use { it.toBitmap() }
                        if (bitmap != null) {
                            barcodeDetector?.detect(
                                InputImage.fromBitmap(bitmap, proxy.imageInfo.rotationDegrees)
                            )
                        }
                    }
                }
            )

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

            // Use the camera object to link our preview use case with the view
            preview.setSurfaceProvider(viewFinder.createSurfaceProvider())

        }, ContextCompat.getMainExecutor(context))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val context = context ?: return
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (context.checkPermissionsGranted(CAMERA_PERMISSION)) {
                    binding?.bindCameraUseCases()
                } else {
                    context.toast("Permissions not granted by the user.")
                }
            }
            REQUEST_CODE_STORAGE -> {
                if (context.checkPermissionsGranted(STORAGE_PERMISSION)) {
                    Gallery.takePicture(this)
                } else {
                    context.toast("Permissions not granted by the user.")
                }
            }
        }
    }

    private fun Context.checkPermissionsGranted(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Gallery.onPictureTaken(requestCode, resultCode, data) { uri ->
            barcodeDetector?.detect(InputImage.fromFilePath(requireContext(), uri))
        }
    }

    companion object {

        private const val REQUEST_CODE_CAMERA = 10
        private const val REQUEST_CODE_STORAGE = 11

        private val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
