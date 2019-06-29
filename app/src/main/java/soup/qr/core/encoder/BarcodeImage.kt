package soup.qr.core.encoder

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class BarcodeImage(private val barcodeFormat: BarcodeFormat) {

    fun create(contents: String, size: Int): Bitmap? {
        return BarcodeEncoder().encodeBitmap(contents, barcodeFormat, size, size)
    }
}
