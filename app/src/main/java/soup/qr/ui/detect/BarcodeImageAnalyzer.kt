package soup.qr.ui.detect

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import soup.qr.core.detector.BarcodeDetector
import soup.qr.core.detector.input.RawImage

class BarcodeImageAnalyzer(
    private val detector: BarcodeDetector
) : ImageAnalysis.Analyzer {

    override fun analyze(proxy: ImageProxy, rotationDegrees: Int) {
        val image = proxy.image ?: return
        if (detector.isInDetecting().not()) {
            detector.detect(RawImage(image, rotationDegrees))
        }
    }
}
