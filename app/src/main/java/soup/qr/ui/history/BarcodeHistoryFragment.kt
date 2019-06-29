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
        val listAdapter = BarcodeHistoryListAdapter {
            viewModel.onBarcodeHistoryClick(it)
        }
        binding.contents.listView.adapter = listAdapter
        viewModel.uiModel.observeState(viewLifecycleOwner) {
            listAdapter.submitList(it.items)
        }
        viewModel.showDetectEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(
                BarcodeHistoryFragmentDirections.actionToDetect()
            )
        }
        viewModel.showResultEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(
                BarcodeHistoryFragmentDirections.actionToResult(it)
            )
        }
        return binding.root
    }
}
