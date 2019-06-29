package soup.qr.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soup.qr.databinding.HistoryFragmentBinding
import soup.qr.ui.BaseFragment
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
        viewModel.uiModel.observeState(viewLifecycleOwner) {
            binding.render(it)
        }
        return binding.root
    }

    private fun HistoryFragmentBinding.render(uiModel: BarcodeHistoryUiModel) {
    }
}
