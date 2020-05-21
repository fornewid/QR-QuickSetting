package soup.qr.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import soup.qr.data.local.BarcodeLocalDataSource
import soup.qr.model.BarcodeHistory

class BarcodeRepositoryImpl(
    private val local: BarcodeLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BarcodeRepository {

    override fun getHistories(): Flow<List<BarcodeHistory>> {
        return local.getHistories().flowOn(ioDispatcher)
    }

    override suspend fun addHistory(history: BarcodeHistory) {
        return withContext(ioDispatcher) {
            local.addHistory(history)
        }
    }

    override suspend fun deleteHistory(history: BarcodeHistory) {
        return withContext(ioDispatcher) {
            local.deleteHistory(history)
        }
    }
}
