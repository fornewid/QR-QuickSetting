package soup.qr.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import soup.qr.databinding.HistoryFragmentBinding
import soup.qr.ui.BaseFragment
import soup.qr.utils.observeEvent
import soup.qr.utils.observeState

class BarcodeHistoryFragment : BaseFragment() {

    private val viewModel: BarcodeHistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HistoryFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.uiModel.observeState(viewLifecycleOwner) {
            binding.render(it)
        }
        viewModel.showDetectEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(
                BarcodeHistoryFragmentDirections.actionToDetect()
            )
        }
        return binding.root
    }

    private fun HistoryFragmentBinding.render(uiModel: BarcodeHistoryUiModel) {
    }
}
