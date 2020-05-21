package soup.qr.ui.magnified

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import soup.qr.model.Barcode

class BarcodeMagnifiedViewModel : ViewModel() {

    private val _uiModel = MutableLiveData<Barcode>()
    val uiModel: LiveData<Barcode>
        get() = _uiModel

    fun onCreated(barcode: Barcode) {
        _uiModel.value = barcode
    }
}
