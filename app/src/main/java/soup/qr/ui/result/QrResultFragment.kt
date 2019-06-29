package soup.qr.ui.result

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.databinding.FragmentResultBinding
import soup.qr.core.encoder.BarcodeImage
import soup.qr.mapper.BarcodeFormatMapper
import soup.qr.model.QrCode

class QrResultFragment : Fragment() {

    private val args: QrResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.render(qrCode = args.qrCode)
        return binding.root
    }

    private fun FragmentResultBinding.render(qrCode: QrCode) {
        if (qrCode is QrCode.Url) {
            barcodeImage.setBarcodeImage(qrCode)
            displayText.text = qrCode.rawValue
            actionButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(qrCode.url)))
            }
        } else {
            findNavController().navigateUp()
        }
    }

    private fun ImageView.setBarcodeImage(qrCode: QrCode) {
        val size = context.resources.getDimensionPixelSize(R.dimen.qr_code_size)
        setImageBitmap(createBarcodeImage(qrCode, size = size))
    }

    private fun createBarcodeImage(qrCode: QrCode, size: Int): Bitmap? {
        return BarcodeFormatMapper.zxingFormatOf(qrCode.format)
            ?.let(::BarcodeImage)
            ?.create(qrCode.rawValue, size)
    }

}
