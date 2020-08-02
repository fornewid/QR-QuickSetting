package soup.qr.core.detector.firebase

import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import soup.qr.core.detector.BarcodeDetector
import timber.log.Timber
import java.util.concurrent.Executor

class MLKitBarcodeDetector(
    lifecycleOwner: LifecycleOwner,
    executor: Executor,
    private val onDetected: (soup.qr.model.Barcode) -> Unit
) : BarcodeDetector {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .setExecutor(executor)
            .build()
    )

    init {
        lifecycleOwner.lifecycle.addObserver(scanner)
    }

    override fun detect(inputImage: InputImage) {
        scanner
            .process(inputImage)
            .addOnSuccessListener {
                it.firstOrNull()?.toModel()?.run(onDetected)
            }
            .addOnFailureListener {
                Timber.w(it)
            }
    }

    private fun Barcode.toModel(): soup.qr.model.Barcode {
        return when (valueType) {
            Barcode.TYPE_URL ->
                soup.qr.model.Barcode.Url(
                    format = format,
                    rawValue = rawValue.orEmpty(),
                    url = rawValue.orEmpty()
                )
            Barcode.TYPE_TEXT ->
                soup.qr.model.Barcode.Text(
                    format = format,
                    rawValue = rawValue.orEmpty()
                )
            else ->
                soup.qr.model.Barcode.Unknown(
                    format = format,
                    rawValue = rawValue.orEmpty()
                )
        }
    }
}
