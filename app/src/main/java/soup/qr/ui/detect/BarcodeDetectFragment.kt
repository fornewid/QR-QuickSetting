package soup.qr.ui.detect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import soup.qr.R
import soup.qr.core.detector.BarcodeDetector
import soup.qr.core.detector.firebase.FirebaseBarcodeDetector
import soup.qr.core.detector.input.bitmapOf
import soup.qr.databinding.DetectBinding
import soup.qr.ui.EventObserver
import soup.qr.ui.detect.BarcodeDetectFragmentDirections.Companion.actionToResult
import soup.qr.ui.setOnDebounceClickListener
import soup.qr.util.Gallery
import soup.qr.util.toast

class BarcodeDetectFragment : Fragment(R.layout.detect) {

    private val viewModel: BarcodeDetectViewModel by viewModels()

    private var binding: DetectBinding? = null
    private var hintAnimation: BarcodeDetectHintAnimation? = null

    private val detector: BarcodeDetector = FirebaseBarcodeDetector()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                startCamera()
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

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun DetectBinding.startCamera() {
        cameraView.bindToLifecycle(viewLifecycleOwner)
        cameraView.setAnalyzer { proxy ->
            proxy.use {
                if (detector.isInDetecting().not()) {
                    it.image?.use { image ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            bitmapOf(image, it.imageInfo.rotationDegrees)?.let { bitmap ->
                                viewModel.onDetected(detector.detect(bitmap))
                            }
                        }
                    }
                }
            }
        }
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
                    binding?.startCamera()
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
        Gallery.onPictureTaken(requestCode, resultCode, data) {
            if (detector.isInDetecting().not()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    bitmapOf(requireContext(), it)
                        ?.let { detector.detect(it) }
                        ?.let { viewModel.onDetected(it) }
                }
            }
        }
    }

    companion object {

        private const val REQUEST_CODE_CAMERA = 10
        private const val REQUEST_CODE_STORAGE = 11

        private val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
