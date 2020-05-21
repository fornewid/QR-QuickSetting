package soup.qr.ui.result

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.databinding.ResultBinding
import soup.qr.ui.result.BarcodeResultFragmentDirections.Companion.actionToMagnified
import soup.qr.ui.setBarcodeImage
import soup.qr.ui.setOnDebounceClickListener

class BarcodeResultFragment : Fragment(R.layout.result) {

    private val args: BarcodeResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(ResultBinding.bind(view)) {
            val barcode = args.barcode
            barcodeImage.setBarcodeImage(barcode.format, barcode.rawValue)
            barcodeImage.setOnDebounceClickListener {
                findNavController().navigate(actionToMagnified(barcode))
            }
            displayText.text = barcode.rawValue
            actionButton.isGone = true
        }
    }
}
