package soup.qr.ui.result

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.core.encoder.BarcodeImage
import soup.qr.databinding.FragmentResultBinding
import soup.qr.mapper.BarcodeFormatMapper
import soup.qr.model.Barcode
import soup.qr.ui.result.BarcodeResultFragmentDirections.Companion.actionToMagnified
import soup.qr.utils.setOnDebounceClickListener

class BarcodeResultFragment : Fragment(R.layout.fragment_result) {

    private val args: BarcodeResultFragmentArgs by navArgs()
    private val viewModel: BarcodeResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentResultBinding.bind(view)) {
            barcodeImage.setOnDebounceClickListener {
                findNavController().navigate(actionToMagnified(args.barcode))
            }
            viewModel.uiModel.observe(viewLifecycleOwner, Observer {
                barcodeImage.setBarcodeImage(it)
                displayText.text = it.rawValue
                actionButton.isGone = true
            })
        }
        viewModel.onCreated(args.barcode)
    }

    private fun ImageView.setBarcodeImage(barcode: Barcode) {
        fun createBarcodeImage(barcode: Barcode): Bitmap? {
            val size = resources.getDimensionPixelSize(R.dimen.qr_code_size)
            return BarcodeFormatMapper.zxingFormatOf(barcode.format)
                ?.let(::BarcodeImage)
                ?.create(barcode.rawValue, size)
        }
        setImageBitmap(createBarcodeImage(barcode))
    }
}
