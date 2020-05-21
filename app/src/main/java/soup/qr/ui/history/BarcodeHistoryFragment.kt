package soup.qr.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import soup.qr.databinding.HistoryFragmentBinding
import soup.qr.ui.EventObserver
import soup.qr.ui.history.BarcodeHistoryFragmentDirections.Companion.actionToDetect
import soup.qr.ui.history.BarcodeHistoryFragmentDirections.Companion.actionToHistoryDelete
import soup.qr.ui.history.BarcodeHistoryFragmentDirections.Companion.actionToResult

class BarcodeHistoryFragment : Fragment() {

    private val viewModel: BarcodeHistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HistoryFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val listAdapter = BarcodeHistoryListAdapter(
            clickListener = viewModel::onBarcodeHistoryClick,
            longClickListener = viewModel::onBarcodeHistoryLongClick
        )
        binding.contents.listView.adapter = listAdapter
        viewModel.uiModel.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it.items)
        })
        viewModel.showDetectEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(actionToDetect())
        })
        viewModel.showResultEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(actionToResult(it))
        })
        viewModel.showDeleteDialogEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(actionToHistoryDelete())
        })
        return binding.root
    }
}
