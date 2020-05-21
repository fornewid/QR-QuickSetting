package soup.qr.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import soup.qr.data.BarcodeRepository
import soup.qr.model.Barcode
import soup.qr.model.BarcodeHistory
import soup.qr.ui.EventLiveData
import soup.qr.ui.MutableEventLiveData
import timber.log.Timber
import javax.inject.Inject

class BarcodeHistoryViewModel @Inject constructor(
    private val repository: BarcodeRepository
) : ViewModel() {

    val uiModel = repository.getHistories()
        .map { BarcodeHistoryUiModel(it) }
        .asLiveData()

    private val _showDetectEvent = MutableEventLiveData<Unit>()
    val showDetectEvent: EventLiveData<Unit>
        get() = _showDetectEvent

    private val _showResultEvent = MutableEventLiveData<Barcode>()
    val showResultEvent: EventLiveData<Barcode>
        get() = _showResultEvent

    private var selectedHistory: BarcodeHistory? = null

    private val _showDeleteDialogEvent = MutableEventLiveData<Unit>()
    val showDeleteDialogEvent: EventLiveData<Unit>
        get() = _showDeleteDialogEvent

    init {
        Timber.d("init: 0x${hashCode().toString(16)}")
    }

    fun onDetectClick() {
        _showDetectEvent.event = Unit
    }

    fun onBarcodeHistoryClick(history: BarcodeHistory) {
        _showResultEvent.event = history.toBarcode()
    }

    fun onBarcodeHistoryLongClick(history: BarcodeHistory) {
        selectedHistory = history
        _showDeleteDialogEvent.event = Unit
    }

    fun onBarcodeHistoryDelete() {
        selectedHistory?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteHistory(it)
            }
        }
    }

    fun onBarcodeHistoryCancel() {
        selectedHistory = null
    }

    private fun BarcodeHistory.toBarcode(): Barcode {
        return Barcode.Unknown(format, rawValue)
    }
}
