package soup.qr.ui.result

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import soup.qr.R
import soup.qr.core.encoder.BarcodeImage
import soup.qr.databinding.FragmentResultBinding
import soup.qr.mapper.BarcodeFormatMapper
import soup.qr.model.Barcode
import soup.qr.utils.observeState
import soup.qr.utils.setOnDebounceClickListener

class BarcodeResultFragment : Fragment() {

    private val args: BarcodeResultFragmentArgs by navArgs()

    private val viewModel: BarcodeResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.barcodeImage.setOnDebounceClickListener {
            findNavController().navigate(
                BarcodeResultFragmentDirections.actionToMagnified(args.barcode)
            )
        }
        viewModel.uiModel.observeState(viewLifecycleOwner) {
            binding.render(barcode = it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onCreated(args.barcode)
    }

    private fun FragmentResultBinding.render(barcode: Barcode) {
        barcodeImage.setBarcodeImage(barcode)
        displayText.text = barcode.rawValue
        actionButton.isGone = true
    }

    private fun ImageView.setBarcodeImage(barcode: Barcode) {
        setImageBitmap(createBarcodeImage(barcode))
    }

    private fun View.createBarcodeImage(barcode: Barcode): Bitmap? {
        val size = context.resources.getDimensionPixelSize(R.dimen.qr_code_size)
        return BarcodeFormatMapper.zxingFormatOf(barcode.format)
            ?.let(::BarcodeImage)
            ?.create(barcode.rawValue, size)
    }
}
