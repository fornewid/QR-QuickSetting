package soup.qr.ui.detect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import soup.qr.Dependency
import soup.qr.data.BarcodeRepository
import soup.qr.model.Barcode
import soup.qr.model.BarcodeHistory
import soup.qr.ui.EventLiveData
import soup.qr.ui.MutableEventLiveData

class BarcodeDetectViewModel(
    private val repository: BarcodeRepository = Dependency.repository
) : ViewModel() {

    private val _showResultEvent = MutableEventLiveData<Barcode>()
    val showResultEvent: EventLiveData<Barcode>
        get() = _showResultEvent

    fun onDetected(barcode: Barcode) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHistory(BarcodeHistory(barcode.format, barcode.rawValue, OffsetDateTime.now()))
            _showResultEvent.postEvent(barcode)
        }
    }
}
