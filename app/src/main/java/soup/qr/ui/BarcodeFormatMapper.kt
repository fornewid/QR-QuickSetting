package soup.qr.ui

import com.google.mlkit.vision.barcode.Barcode
import com.google.zxing.BarcodeFormat

fun zxingFormatOf(firebaseFormat: Int): BarcodeFormat? {
    return when (firebaseFormat) {
        Barcode.FORMAT_CODE_128 -> BarcodeFormat.CODE_128
        Barcode.FORMAT_CODE_39 -> BarcodeFormat.CODE_39
        Barcode.FORMAT_CODE_93 -> BarcodeFormat.CODE_93
        Barcode.FORMAT_CODABAR -> BarcodeFormat.CODABAR
        Barcode.FORMAT_DATA_MATRIX -> BarcodeFormat.DATA_MATRIX
        Barcode.FORMAT_EAN_13 -> BarcodeFormat.EAN_13
        Barcode.FORMAT_EAN_8 -> BarcodeFormat.EAN_8
        Barcode.FORMAT_ITF -> BarcodeFormat.ITF
        Barcode.FORMAT_QR_CODE -> BarcodeFormat.QR_CODE
        Barcode.FORMAT_UPC_A -> BarcodeFormat.UPC_A
        Barcode.FORMAT_UPC_E -> BarcodeFormat.UPC_E
        Barcode.FORMAT_PDF417 -> BarcodeFormat.PDF_417
        Barcode.FORMAT_AZTEC -> BarcodeFormat.AZTEC
        else -> null
    }
}

fun firebaseFormatOf(zxingFormat: BarcodeFormat): Int? {
    return when (zxingFormat) {
        BarcodeFormat.CODE_128 -> Barcode.FORMAT_CODE_128
        BarcodeFormat.CODE_39 -> Barcode.FORMAT_CODE_39
        BarcodeFormat.CODE_93 -> Barcode.FORMAT_CODE_93
        BarcodeFormat.CODABAR -> Barcode.FORMAT_CODABAR
        BarcodeFormat.DATA_MATRIX -> Barcode.FORMAT_DATA_MATRIX
        BarcodeFormat.EAN_13 -> Barcode.FORMAT_EAN_13
        BarcodeFormat.EAN_8 -> Barcode.FORMAT_EAN_8
        BarcodeFormat.ITF -> Barcode.FORMAT_ITF
        BarcodeFormat.QR_CODE -> Barcode.FORMAT_QR_CODE
        BarcodeFormat.UPC_A -> Barcode.FORMAT_UPC_A
        BarcodeFormat.UPC_E -> Barcode.FORMAT_UPC_E
        BarcodeFormat.PDF_417 -> Barcode.FORMAT_PDF417
        BarcodeFormat.AZTEC -> Barcode.FORMAT_AZTEC
        else -> null
    }
}
