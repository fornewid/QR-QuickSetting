package soup.qr.ui.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import soup.qr.R
import soup.qr.databinding.HistoryFragmentBinding
import soup.qr.ui.EventObserver
import soup.qr.ui.history.BarcodeHistoryFragmentDirections.Companion.actionToDetect
import soup.qr.ui.history.BarcodeHistoryFragmentDirections.Companion.actionToHistoryDelete
import soup.qr.ui.history.BarcodeHistoryFragmentDirections.Companion.actionToResult
import soup.qr.utils.setOnDebounceClickListener

class BarcodeHistoryFragment : Fragment(R.layout.history_fragment) {

    private val viewModel: BarcodeHistoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(HistoryFragmentBinding.bind(view)) {
            val listAdapter = BarcodeHistoryListAdapter(
                clickListener = viewModel::onBarcodeHistoryClick,
                longClickListener = viewModel::onBarcodeHistoryLongClick
            )
            listView.adapter = listAdapter
            detectButton.setOnDebounceClickListener {
                viewModel.onDetectClick()
            }

            viewModel.uiModel.observe(viewLifecycleOwner, Observer {
                listAdapter.submitList(it.items)
                emptyGroup.isVisible = it.isEmpty()
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
        }
    }
}
