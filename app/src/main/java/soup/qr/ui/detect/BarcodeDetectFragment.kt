package soup.qr.ui.detect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import soup.qr.core.detector.BarcodeDetector
import soup.qr.core.detector.firebase.FirebaseBarcodeDetector
import soup.qr.core.detector.input.RawImage
import soup.qr.databinding.FragmentDetectBinding
import soup.qr.model.Barcode
import soup.qr.ui.BaseFragment
import soup.qr.utils.observeEvent

class BarcodeDetectFragment : BaseFragment() {

    private val viewModel: BarcodeDetectViewModel by viewModel()

    private lateinit var binding: FragmentDetectBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initViewState()
        viewModel.showResultEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(
                BarcodeDetectFragmentDirections.actionToResult(barcode = it)
            )
        }
        return binding.root
    }

    private fun FragmentDetectBinding.initViewState() {
        hintAnimation = BarcodeDetectHintAnimation(this)

        if (allPermissionsGranted(root.context)) {
            startCamera()
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
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

    private fun FragmentDetectBinding.startCamera() {
        cameraView.bindToLifecycle(viewLifecycleOwner)
        cameraView.setAnalyzer { image, rotationDegrees ->
            val frameImage = image.image
            if (frameImage != null && detector.isInDetecting().not()) {
                detector.detect(RawImage(frameImage, rotationDegrees))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val context: Context = binding.root.context
            if (allPermissionsGranted(context)) {
                binding.startCamera()
            } else {
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
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
