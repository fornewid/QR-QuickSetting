package soup.qr.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "barcode_history", primaryKeys = ["format", "raw_value"])
data class BarcodeHistoryEntity(
    @ColumnInfo(name = "format") val format: Int,
    @ColumnInfo(name = "raw_value") val rawValue: String,
    @ColumnInfo(name = "last_detected_at") val lastDetectedAt: Long
)
