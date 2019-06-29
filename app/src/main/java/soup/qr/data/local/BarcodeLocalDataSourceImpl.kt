package soup.qr.data.local

import io.reactivex.Completable
import io.reactivex.Observable
import soup.qr.data.local.entity.BarcodeHistory

class BarcodeLocalDataSourceImpl(
    private val dao: BarcodeDao
) : BarcodeLocalDataSource {

    override fun getHistories(): Observable<List<BarcodeHistory>> {
        return dao.getBarcode()
    }

    override fun addHistory(history: BarcodeHistory): Completable {
        return dao.insert(history)
    }
}
