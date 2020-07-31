package soup.qr.core.detector.firebase

import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import soup.qr.core.detector.BarcodeDetector
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseBarcodeDetector : BarcodeDetector {

    private val coreDetector = BarcodeScanning.getClient()

    private val isInDetecting = AtomicBoolean(false)

    // workaround
    private var lastDetectTime: Long = 0

    override fun isInDetecting(): Boolean {
        return isInDetecting.get() || hasAftertasteYet()
    }

    override suspend fun detect(bitmap: Bitmap): soup.qr.model.Barcode? {
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

    private suspend fun detectIn(bitmap: Bitmap): List<soup.qr.model.Barcode> {
        return suspendCoroutine { continuation ->
            coreDetector
                .process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener {
                    val barcodeList = it.mapNotNull { barcode ->
                        when (barcode.valueType) {
                            Barcode.TYPE_URL ->
                                soup.qr.model.Barcode.Url(
                                    format = barcode.format,
                                    rawValue = barcode.rawValue.orEmpty(),
                                    url = barcode.rawValue.orEmpty()
                                )
                            Barcode.TYPE_TEXT ->
                                soup.qr.model.Barcode.Text(
                                    format = barcode.format,
                                    rawValue = barcode.rawValue.orEmpty()
                                )
                            else ->
                                soup.qr.model.Barcode.Unknown(
                                    format = barcode.format,
                                    rawValue = barcode.rawValue.orEmpty()
                                )
                        }
                    }
                    continuation.resume(barcodeList)
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
