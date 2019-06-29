package soup.qr.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.qr.data.BarcodeRepository
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
}
