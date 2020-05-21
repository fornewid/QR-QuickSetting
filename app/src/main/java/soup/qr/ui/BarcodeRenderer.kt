package soup.qr.ui

import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import soup.qr.R
import soup.qr.core.encoder.BarcodeImage

fun TextView.setBarcodeFormatText(barcodeFormat: Int) {
    text = when (barcodeFormat) {
        FirebaseVisionBarcode.FORMAT_CODE_128 -> "CODE_128"
        FirebaseVisionBarcode.FORMAT_CODE_39 -> "CODE_39"
        FirebaseVisionBarcode.FORMAT_CODE_93 -> "CODE_93"
        FirebaseVisionBarcode.FORMAT_CODABAR -> "CODABAR"
        FirebaseVisionBarcode.FORMAT_DATA_MATRIX -> "DATA_MATRIX"
        FirebaseVisionBarcode.FORMAT_EAN_13 -> "EAN_13"
        FirebaseVisionBarcode.FORMAT_EAN_8 -> "EAN_8"
        FirebaseVisionBarcode.FORMAT_ITF -> "ITF"
        FirebaseVisionBarcode.FORMAT_QR_CODE -> "QR_CODE"
        FirebaseVisionBarcode.FORMAT_UPC_A -> "UPC_A"
        FirebaseVisionBarcode.FORMAT_UPC_E -> "UPC_E"
        FirebaseVisionBarcode.FORMAT_PDF417 -> "PDF_417"
        FirebaseVisionBarcode.FORMAT_AZTEC -> "AZTEC"
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
