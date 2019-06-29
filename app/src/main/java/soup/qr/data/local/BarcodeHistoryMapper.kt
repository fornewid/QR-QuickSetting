package soup.qr.data.local

import soup.qr.data.local.entity.BarcodeHistoryEntity
import soup.qr.model.BarcodeHistory

fun BarcodeHistory.toEntity(): BarcodeHistoryEntity {
    return BarcodeHistoryEntity(format, rawValue, lastDetectedAt)
}

fun BarcodeHistoryEntity.toModel(): BarcodeHistory {
    return BarcodeHistory(format, rawValue, lastDetectedAt)
}
