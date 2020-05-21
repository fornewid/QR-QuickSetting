package soup.qr.ui.magnified

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.core.encoder.BarcodeImage
import soup.qr.databinding.MagnifiedFragmentBinding
import soup.qr.mapper.BarcodeFormatMapper
import soup.qr.model.Barcode
import soup.qr.ui.result.BarcodeResultFragmentArgs
import soup.qr.utils.setOnDebounceClickListener

class BarcodeMagnifiedFragment : Fragment(R.layout.magnified_fragment) {

    private val args: BarcodeResultFragmentArgs by navArgs()
    private val viewModel: BarcodeMagnifiedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(MagnifiedFragmentBinding.bind(view)) {
            barcodeImage.setOnDebounceClickListener {
                findNavController().navigateUp()
            }
            viewModel.uiModel.observe(viewLifecycleOwner, Observer {
                barcodeImage.setBarcodeImage(it)
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