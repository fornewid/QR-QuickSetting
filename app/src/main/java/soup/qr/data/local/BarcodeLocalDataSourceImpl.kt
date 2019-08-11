package soup.qr.data.local

import io.reactivex.Completable
import io.reactivex.Observable
import soup.qr.data.local.entity.BarcodeHistoryEntity
import soup.qr.model.BarcodeHistory

class BarcodeLocalDataSourceImpl(
    private val dao: BarcodeDao
) : BarcodeLocalDataSource {

    override fun getHistories(): Observable<List<BarcodeHistory>> {
        return dao.getBarcode().map { it.map(BarcodeHistoryEntity::toModel) }
    }

    override fun addHistory(history: BarcodeHistory): Completable {
        return dao.insert(history.toEntity())
    }

    override fun deleteHistory(history: BarcodeHistory): Completable {
        return dao.delete(history.toEntity())
    }
}
