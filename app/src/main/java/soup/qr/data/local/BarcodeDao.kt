package soup.qr.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import soup.qr.data.local.entity.BarcodeHistoryEntity

@Dao
interface BarcodeDao {

    @Query("SELECT * FROM barcode_history ORDER BY datetime(last_detected_at) DESC")
    fun getBarcode(): Observable<List<BarcodeHistoryEntity>>

    @Insert(onConflict = REPLACE)
    fun insert(history: BarcodeHistoryEntity): Completable

    @Delete
    fun delete(history: BarcodeHistoryEntity): Completable

    @Query("DELETE FROM barcode_history")
    fun deleteAll(): Completable
}
