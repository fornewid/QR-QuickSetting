package soup.qr.core.detector.input

class RawImage(
    val data: ByteArray,
    val width: Int,
    val height: Int,
    val format: RawImageFormat,
    val rotationDegrees: Int
)
