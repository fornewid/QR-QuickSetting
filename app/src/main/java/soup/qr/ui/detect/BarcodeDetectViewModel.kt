package soup.qr.ui.detect

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime
import soup.qr.data.BarcodeRepository
import soup.qr.model.Barcode
import soup.qr.model.BarcodeHistory
import soup.qr.ui.BaseViewModel
import soup.qr.ui.EventLiveData
import soup.qr.ui.MutableEventLiveData
import javax.inject.Inject

class BarcodeDetectViewModel @Inject constructor(
    private val repository: BarcodeRepository
) : BaseViewModel() {

    private val _showResultEvent = MutableEventLiveData<Barcode>()
    val showResultEvent: EventLiveData<Barcode>
        get() = _showResultEvent

    fun onDetected(barcode: Barcode) {
        repository
            .addHistory(
                BarcodeHistory(
                    barcode.format,
                    barcode.rawValue,
                    OffsetDateTime.now()
                )
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _showResultEvent.event = barcode
            }
            .disposeOnCleared()
    }
}
