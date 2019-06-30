package soup.qr.ui.history

import soup.qr.model.BarcodeHistory

class BarcodeHistoryUiModel(
    val items: List<BarcodeHistory>
) {

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }
}
