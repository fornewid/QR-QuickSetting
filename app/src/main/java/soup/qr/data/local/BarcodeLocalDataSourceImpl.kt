package soup.qr.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soup.qr.data.local.entity.BarcodeHistoryEntity
import soup.qr.model.BarcodeHistory

class BarcodeLocalDataSourceImpl(
    private val dao: BarcodeDao
) : BarcodeLocalDataSource {

    override fun getHistories(): Flow<List<BarcodeHistory>> {
        return dao.getBarcode().map { it.map(BarcodeHistoryEntity::toModel) }
    }

    override suspend fun addHistory(history: BarcodeHistory) {
        return dao.insert(history.toEntity())
    }

    override suspend fun deleteHistory(history: BarcodeHistory) {
        return dao.delete(history.toEntity())
    }
}
