package soup.qr.detector.input

class RawImage(
    val format: RawImageFormat,
    val data: ByteArray,
    val width: Int,
    val height: Int,
    val rotationDegrees: Int
)
