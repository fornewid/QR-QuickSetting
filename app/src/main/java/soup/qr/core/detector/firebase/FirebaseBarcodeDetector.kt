package soup.qr.core.detector.firebase

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import soup.qr.core.detector.BarcodeDetector
import soup.qr.core.detector.input.RawImage
import soup.qr.model.Barcode
import java.util.concurrent.atomic.AtomicBoolean

class FirebaseBarcodeDetector : BarcodeDetector {

    private val coreDetector: FirebaseVisionBarcodeDetector

    private val isInDetecting = AtomicBoolean(false)

    private var callback: BarcodeDetector.Callback? = null

    // workaround
    private var lastDetectTime: Long = 0

    init {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
            .build()
        coreDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
    }

    override fun setCallback(callback: BarcodeDetector.Callback?) {
        this.callback = callback
    }

    override fun isInDetecting(): Boolean {
        return isInDetecting.get() || hasAftertasteYet()
    }

    override fun detect(image: RawImage)  {
        if (isInDetecting.compareAndSet(false, true)) {
            fun complete() {
                recordDetectTime()
                isInDetecting.set(false)
            }
            val bitmap = image.toBitmap()
            detectIn(bitmap,
                onSuccess = {
                    val barcode = it.firstOrNull()
                    if (barcode != null) {
                        callback?.onDetected(barcode)
                        complete()
                    } else {
                        detectIn(bitmap.inverted(),
                            onSuccess = {
                                val barcode = it.firstOrNull()
                                if (barcode != null) {
                                    callback?.onDetected(barcode)
                                } else {
                                    callback?.onDetectFailed()
                                }
                                complete()
                            },
                            onError = {
                                callback?.onDetectFailed()
                                complete()
                            }
                        )
                    }
                },
                onError = {
                    callback?.onDetectFailed()
                    complete()
                }
            )
        }
    }

    private inline fun detectIn(
        bitmap: Bitmap,
        crossinline onSuccess: (List<Barcode>) -> Unit,
        crossinline onError: (Throwable) -> Unit
    ) {
        coreDetector
            .detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
            .addOnSuccessListener {
                it.mapNotNull { barcode ->
                    when (barcode.valueType) {
                        FirebaseVisionBarcode.TYPE_URL ->
                            Barcode.Url(
                                format = barcode.format,
                                rawValue = barcode.rawValue.orEmpty(),
                                url = barcode.rawValue.orEmpty()
                            )
                        FirebaseVisionBarcode.TYPE_TEXT ->
                            Barcode.Text(
                                format = barcode.format,
                                rawValue = barcode.rawValue.orEmpty()
                            )
                        else ->
                            Barcode.Unknown(
                                format = barcode.format,
                                rawValue = barcode.rawValue.orEmpty()
                            )
                    }
                }.run(onSuccess)
            }
            .addOnFailureListener {
                onError(it)
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
