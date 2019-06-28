package soup.qr.detector.output

import kotlinx.android.parcel.Parcelize

@Parcelize
class UrlQrCode(
    val rawValue: String,
    val displayText: String,
    val url: String
) : QrCode
