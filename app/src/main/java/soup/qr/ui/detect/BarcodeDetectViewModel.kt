package soup.qr.ui.detect

import soup.qr.model.Barcode
import soup.qr.ui.BaseViewModel
import soup.qr.ui.EventLiveData
import soup.qr.ui.MutableEventLiveData
import javax.inject.Inject

class BarcodeDetectViewModel @Inject constructor() : BaseViewModel() {

    private val _showResultEvent = MutableEventLiveData<Barcode>()
    val showResultEvent: EventLiveData<Barcode>
        get() = _showResultEvent

    fun onDetected(barcode: Barcode) {
        _showResultEvent.event = barcode
    }
}
