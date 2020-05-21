package soup.qr.ui.history.delete

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import soup.qr.R
import soup.qr.ui.history.BarcodeHistoryViewModel

class BarcodeDeleteDialogFragment : AppCompatDialogFragment() {

    private val viewModel: BarcodeHistoryViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            .setTitle(R.string.history_delete_title)
            .setMessage(R.string.history_delete_message)
            .setPositiveButton(R.string.history_delete_button_positive) { _, _ ->
                viewModel.onBarcodeHistoryDelete()
            }
            .setNegativeButton(R.string.history_delete_button_negative) { _, _ ->
                viewModel.onBarcodeHistoryCancel()
            }
            .create()
    }
}
