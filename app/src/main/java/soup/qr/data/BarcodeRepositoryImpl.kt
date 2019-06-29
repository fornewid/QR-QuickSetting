package soup.qr.data

import io.reactivex.Completable
import io.reactivex.Observable
import soup.qr.data.local.BarcodeLocalDataSource
import soup.qr.data.local.entity.BarcodeHistory

class BarcodeRepositoryImpl(
    private val local: BarcodeLocalDataSource
) : BarcodeRepository {

    override fun getHistories(): Observable<List<BarcodeHistory>> {
        return local.getHistories()
    }

    override fun addHistory(history: BarcodeHistory): Completable {
        return local.addHistory(history)
    }
}
