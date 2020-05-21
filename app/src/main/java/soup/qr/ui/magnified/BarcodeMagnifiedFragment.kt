package soup.qr.ui.magnified

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.databinding.MagnifiedBinding
import soup.qr.ui.result.BarcodeResultFragmentArgs
import soup.qr.ui.setBarcodeImage
import soup.qr.ui.setOnDebounceClickListener

class BarcodeMagnifiedFragment : Fragment(R.layout.magnified) {

    private val args: BarcodeResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(MagnifiedBinding.bind(view)) {
            val barcode = args.barcode
            barcodeImage.setBarcodeImage(barcode.format, barcode.rawValue)
            barcodeImage.setOnDebounceClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
