package soup.qr.ui.magnified

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import soup.qr.model.Barcode
import soup.qr.ui.BaseViewModel
import javax.inject.Inject

class BarcodeMagnifiedViewModel @Inject constructor() : BaseViewModel() {

    private val _uiModel = MutableLiveData<Barcode>()
    val uiModel: LiveData<Barcode>
        get() = _uiModel

    fun onCreated(barcode: Barcode) {
        _uiModel.value = barcode
    }
}
