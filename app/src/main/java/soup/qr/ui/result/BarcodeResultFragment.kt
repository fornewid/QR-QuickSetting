package soup.qr.ui.result

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.core.encoder.BarcodeImage
import soup.qr.databinding.FragmentResultBinding
import soup.qr.mapper.BarcodeFormatMapper
import soup.qr.model.Barcode
import soup.qr.ui.BaseFragment

class BarcodeResultFragment : BaseFragment() {

    private val args: BarcodeResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.render(barcode = args.barcode)
        return binding.root
    }

    private fun FragmentResultBinding.render(barcode: Barcode) {
        if (barcode is Barcode.Url) {
            barcodeImage.setBarcodeImage(barcode)
            displayText.text = barcode.rawValue
            actionButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(barcode.url)))
            }
        } else {
            findNavController().navigateUp()
        }
    }

    private fun ImageView.setBarcodeImage(barcode: Barcode) {
        val size = context.resources.getDimensionPixelSize(R.dimen.qr_code_size)
        setImageBitmap(createBarcodeImage(barcode, size = size))
    }

    private fun createBarcodeImage(barcode: Barcode, size: Int): Bitmap? {
        return BarcodeFormatMapper.zxingFormatOf(barcode.format)
            ?.let(::BarcodeImage)
            ?.create(barcode.rawValue, size)
    }

}
