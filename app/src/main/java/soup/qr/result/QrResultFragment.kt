package soup.qr.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.databinding.FragmentResultBinding
import soup.qr.detector.output.QrCode
import soup.qr.detector.output.UrlQrCode

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
        if (qrCode is UrlQrCode) {
            displayText.text = qrCode.displayText
            actionButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(qrCode.url)))
            }
        } else {
            findNavController().navigateUp()
        }
    }
}
