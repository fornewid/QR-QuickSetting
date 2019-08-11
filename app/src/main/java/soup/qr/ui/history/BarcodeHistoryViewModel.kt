package soup.qr.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.qr.data.BarcodeRepository
import soup.qr.model.Barcode
import soup.qr.model.BarcodeHistory
import soup.qr.ui.BaseViewModel
import soup.qr.ui.EventLiveData
import soup.qr.ui.MutableEventLiveData
import timber.log.Timber
import javax.inject.Inject

class BarcodeHistoryViewModel @Inject constructor(
    private val repository: BarcodeRepository
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<BarcodeHistoryUiModel>()
    val uiModel: LiveData<BarcodeHistoryUiModel>
        get() = _uiModel

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
        repository.getHistories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _uiModel.value = BarcodeHistoryUiModel(it)
            }
            .disposeOnCleared()
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
            repository.deleteHistory(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
                .disposeOnCleared()
        }
    }

    fun onBarcodeHistoryCancel() {
        selectedHistory = null
    }

    private fun BarcodeHistory.toBarcode(): Barcode {
        return Barcode.Unknown(format, rawValue)
    }
}
