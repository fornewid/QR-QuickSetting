package soup.qr.core.detector.firebase

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import soup.qr.core.detector.QrCodeDetector
import soup.qr.core.detector.input.RawImage
import soup.qr.model.QrCode
import java.util.concurrent.atomic.AtomicBoolean

class FirebaseQrCodeDetector : QrCodeDetector {

    private val coreDetector: FirebaseVisionBarcodeDetector

    private val isInDetecting = AtomicBoolean(false)

    private var callback: QrCodeDetector.Callback? = null

    // workaround
    private var lastDetectTime: Long = 0

    init {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
            .build()
        coreDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
    }

    override fun isInDetecting(): Boolean {
        return isInDetecting.get() || hasAftertasteYet()
    }

    override fun detect(image: RawImage) {
        if (isInDetecting.compareAndSet(false, true)) {
            doOnDetectComplete(image) {
                recordDetectTime()
                isInDetecting.set(false)
            }
        }
    }

    override fun setCallback(callback: QrCodeDetector.Callback?) {
        this.callback = callback
    }

    private inline fun doOnDetectComplete(
        rawImage: RawImage,
        crossinline completeAction: () -> Unit
    ) {
        coreDetector.detectInImage(FirebaseVisionImage.from(rawImage))
            .addOnSuccessListener {
                val barcode = it.find { it.valueType == FirebaseVisionBarcode.TYPE_URL }
                if (barcode != null) {
                    val qrCode = QrCode.Url(
                        format = barcode.format,
                        rawValue = barcode.rawValue.orEmpty(),
                        url = barcode.rawValue.orEmpty()
                    )
                    callback?.onDetected(qrCode)
                } else if (it.isNotEmpty()) {
                    callback?.onDetectFailed()
                } else {
                    callback?.onIdle()
                }
            }
            .addOnFailureListener {
                callback?.onDetectFailed()
            }
            .addOnCompleteListener {
                completeAction()
            }
    }

    private fun hasAftertasteYet(): Boolean {
        val now = System.currentTimeMillis()
        return now - lastDetectTime < AFTERTASTE_INTERVAL
    }

    private fun recordDetectTime() {
        lastDetectTime = System.currentTimeMillis()
    }

    companion object {

        private const val AFTERTASTE_INTERVAL: Long = 300
    }
}