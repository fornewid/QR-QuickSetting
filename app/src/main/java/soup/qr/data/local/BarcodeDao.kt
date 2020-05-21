package soup.qr.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import soup.qr.data.local.entity.BarcodeHistoryEntity

@Dao
interface BarcodeDao {

    @Query("SELECT * FROM barcode_history ORDER BY datetime(last_detected_at) DESC")
    fun getBarcode(): Flow<List<BarcodeHistoryEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(history: BarcodeHistoryEntity)

    @Delete
    suspend fun delete(history: BarcodeHistoryEntity)

    @Query("DELETE FROM barcode_history")
    suspend fun deleteAll()
}
