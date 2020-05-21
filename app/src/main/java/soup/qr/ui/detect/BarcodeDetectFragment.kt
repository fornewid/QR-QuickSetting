package soup.qr.ui.detect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import soup.qr.R
import soup.qr.core.detector.BarcodeDetector
import soup.qr.core.detector.firebase.FirebaseBarcodeDetector
import soup.qr.core.detector.input.RawImage
import soup.qr.databinding.DetectBinding
import soup.qr.model.Barcode
import soup.qr.ui.EventObserver
import soup.qr.ui.detect.BarcodeDetectFragmentDirections.Companion.actionToResult

class BarcodeDetectFragment : Fragment(R.layout.detect) {

    private val viewModel: BarcodeDetectViewModel by viewModels()

    private var binding: DetectBinding? = null
    private var hintAnimation: BarcodeDetectHintAnimation? = null

    private val detector: BarcodeDetector = FirebaseBarcodeDetector().apply {
        setCallback(object : BarcodeDetector.Callback {

            override fun onDetected(barcode: Barcode) {
                hintAnimation?.onSuccess()
                viewModel.onDetected(barcode)
            }

            override fun onDetectFailed() {
                hintAnimation?.onError()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(DetectBinding.bind(view)) {
            hintAnimation = BarcodeDetectHintAnimation(this)

            viewModel.showResultEvent.observe(viewLifecycleOwner, EventObserver {
                findNavController().navigate(actionToResult(barcode = it))
            })

            binding = this

            if (allPermissionsGranted(view.context)) {
                startCamera()
            } else {
                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
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

    private fun DetectBinding.startCamera() {
        cameraView.bindToLifecycle(viewLifecycleOwner)
        cameraView.setAnalyzer { proxy ->
            proxy.use {
                it.image?.use {
                    if (detector.isInDetecting().not()) {
                        detector.detect(RawImage(it, proxy.imageInfo.rotationDegrees))
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
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val context = context ?: return
            if (allPermissionsGranted(context)) {
                binding?.startCamera()
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
