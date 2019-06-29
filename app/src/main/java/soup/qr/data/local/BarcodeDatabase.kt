package soup.qr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.qr.data.local.entity.BarcodeHistoryEntity

@Database(entities = [BarcodeHistoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(BarcodeTypeConverters::class)
abstract class BarcodeDatabase : RoomDatabase() {

    abstract fun barcodeDao(): BarcodeDao
}
