package soup.qr.model

import org.threeten.bp.OffsetDateTime

data class BarcodeHistory(
    val format: Int,
    val rawValue: String,
    val lastDetectedAt: OffsetDateTime
)
