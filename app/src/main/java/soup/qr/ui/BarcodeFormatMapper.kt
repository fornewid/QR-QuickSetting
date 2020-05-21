package soup.qr.ui

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.zxing.BarcodeFormat

fun zxingFormatOf(firebaseFormat: Int): BarcodeFormat? {
    return when (firebaseFormat) {
        FirebaseVisionBarcode.FORMAT_CODE_128 -> BarcodeFormat.CODE_128
        FirebaseVisionBarcode.FORMAT_CODE_39 -> BarcodeFormat.CODE_39
        FirebaseVisionBarcode.FORMAT_CODE_93 -> BarcodeFormat.CODE_93
        FirebaseVisionBarcode.FORMAT_CODABAR -> BarcodeFormat.CODABAR
        FirebaseVisionBarcode.FORMAT_DATA_MATRIX -> BarcodeFormat.DATA_MATRIX
        FirebaseVisionBarcode.FORMAT_EAN_13 -> BarcodeFormat.EAN_13
        FirebaseVisionBarcode.FORMAT_EAN_8 -> BarcodeFormat.EAN_8
        FirebaseVisionBarcode.FORMAT_ITF -> BarcodeFormat.ITF
        FirebaseVisionBarcode.FORMAT_QR_CODE -> BarcodeFormat.QR_CODE
        FirebaseVisionBarcode.FORMAT_UPC_A -> BarcodeFormat.UPC_A
        FirebaseVisionBarcode.FORMAT_UPC_E -> BarcodeFormat.UPC_E
        FirebaseVisionBarcode.FORMAT_PDF417 -> BarcodeFormat.PDF_417
        FirebaseVisionBarcode.FORMAT_AZTEC -> BarcodeFormat.AZTEC
        else -> null
    }
}

fun firebaseFormatOf(zxingFormat: BarcodeFormat): Int? {
    return when (zxingFormat) {
        BarcodeFormat.CODE_128 -> FirebaseVisionBarcode.FORMAT_CODE_128
        BarcodeFormat.CODE_39 -> FirebaseVisionBarcode.FORMAT_CODE_39
        BarcodeFormat.CODE_93 -> FirebaseVisionBarcode.FORMAT_CODE_93
        BarcodeFormat.CODABAR -> FirebaseVisionBarcode.FORMAT_CODABAR
        BarcodeFormat.DATA_MATRIX -> FirebaseVisionBarcode.FORMAT_DATA_MATRIX
        BarcodeFormat.EAN_13 -> FirebaseVisionBarcode.FORMAT_EAN_13
        BarcodeFormat.EAN_8 -> FirebaseVisionBarcode.FORMAT_EAN_8
        BarcodeFormat.ITF -> FirebaseVisionBarcode.FORMAT_ITF
        BarcodeFormat.QR_CODE -> FirebaseVisionBarcode.FORMAT_QR_CODE
        BarcodeFormat.UPC_A -> FirebaseVisionBarcode.FORMAT_UPC_A
        BarcodeFormat.UPC_E -> FirebaseVisionBarcode.FORMAT_UPC_E
        BarcodeFormat.PDF_417 -> FirebaseVisionBarcode.FORMAT_PDF417
        BarcodeFormat.AZTEC -> FirebaseVisionBarcode.FORMAT_AZTEC
        else -> null
    }
}
