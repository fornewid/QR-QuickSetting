package soup.qr.data.local

import kotlinx.coroutines.flow.Flow
import soup.qr.model.BarcodeHistory

interface BarcodeLocalDataSource {

    fun getHistories(): Flow<List<BarcodeHistory>>

    suspend fun addHistory(history: BarcodeHistory)

    suspend fun deleteHistory(history: BarcodeHistory)
}
