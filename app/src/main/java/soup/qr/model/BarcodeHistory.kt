package soup.qr.model

data class BarcodeHistory(
    val format: Int,
    val rawValue: String,
    val lastDetectedAt: Long
)
