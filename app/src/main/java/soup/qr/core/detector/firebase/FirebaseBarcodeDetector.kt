package soup.qr.core.detector.firebase

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import soup.qr.core.detector.BarcodeDetector
import soup.qr.model.Barcode
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseBarcodeDetector : BarcodeDetector {

    private val coreDetector = FirebaseVision.getInstance().visionBarcodeDetector

    private val isInDetecting = AtomicBoolean(false)

    // workaround
    private var lastDetectTime: Long = 0

    override fun isInDetecting(): Boolean {
        return isInDetecting.get() || hasAftertasteYet()
    }

    override suspend fun detect(bitmap: Bitmap): Barcode? {
        return if (isInDetecting.compareAndSet(false, true)) {
            withContext(Dispatchers.Default) {
                val original = async { detectIn(bitmap) }
                val inverted = async { detectIn(bitmap.inverted()) }
                try {
                    (original.await() + inverted.await()).firstOrNull()
                } catch (throwable: Throwable) {
                    Timber.w(throwable)
                    null
                } finally {
                    lastDetectTime = System.currentTimeMillis()
                    isInDetecting.set(false)
                }
            }
        } else {
            null
        }
    }

    private suspend fun detectIn(bitmap: Bitmap): List<Barcode> {
        return suspendCoroutine { continuation ->
            coreDetector
                .detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener {
                    val barcodes = it.mapNotNull { barcode ->
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
                    }
                    continuation.resume(barcodes)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    private fun hasAftertasteYet(): Boolean {
        val now = System.currentTimeMillis()
        return now - lastDetectTime < AFTERTASTE_INTERVAL
    }

    companion object {

        private const val AFTERTASTE_INTERVAL: Long = 300
    }
}
