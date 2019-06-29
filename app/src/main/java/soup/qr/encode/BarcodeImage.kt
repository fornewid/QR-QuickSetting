package soup.qr.encode

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat

object BarcodeImage {

    fun qrCodeFrom(contents: String, size: Int): Bitmap? {
        return BarcodeEncoder().encodeBitmap(contents, BarcodeFormat.QR_CODE, size, size)
    }
}
