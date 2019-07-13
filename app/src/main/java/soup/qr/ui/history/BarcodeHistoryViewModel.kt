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
import javax.inject.Inject

class BarcodeHistoryViewModel @Inject constructor(
    repository: BarcodeRepository
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

    private val _showMagnifiedEvent = MutableEventLiveData<Barcode>()
    val showMagnifiedEvent: EventLiveData<Barcode>
        get() = _showMagnifiedEvent

    init {
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
        _showMagnifiedEvent.event = history.toBarcode()
    }

    private fun BarcodeHistory.toBarcode(): Barcode {
        return Barcode.Unknown(format, rawValue)
    }
}
