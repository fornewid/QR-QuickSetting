package soup.qr.ui

import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.barcode.Barcode
import soup.qr.R
import soup.qr.core.encoder.BarcodeImage

fun TextView.setBarcodeFormatText(barcodeFormat: Int) {
    text = when (barcodeFormat) {
        Barcode.FORMAT_CODE_128 -> "CODE_128"
        Barcode.FORMAT_CODE_39 -> "CODE_39"
        Barcode.FORMAT_CODE_93 -> "CODE_93"
        Barcode.FORMAT_CODABAR -> "CODABAR"
        Barcode.FORMAT_DATA_MATRIX -> "DATA_MATRIX"
        Barcode.FORMAT_EAN_13 -> "EAN_13"
        Barcode.FORMAT_EAN_8 -> "EAN_8"
        Barcode.FORMAT_ITF -> "ITF"
        Barcode.FORMAT_QR_CODE -> "QR_CODE"
        Barcode.FORMAT_UPC_A -> "UPC_A"
        Barcode.FORMAT_UPC_E -> "UPC_E"
        Barcode.FORMAT_PDF417 -> "PDF_417"
        Barcode.FORMAT_AZTEC -> "AZTEC"
        else -> null
    }
}

fun ImageView.setBarcodeImage(barcodeFormat: Int, barcodeRawValue: String) {
    val size = resources.getDimensionPixelSize(R.dimen.qr_code_size)
    val barcodeBitmap = zxingFormatOf(barcodeFormat)?.let {
        BarcodeImage(it).create(barcodeRawValue, size)
    }
    setImageBitmap(barcodeBitmap)
}
