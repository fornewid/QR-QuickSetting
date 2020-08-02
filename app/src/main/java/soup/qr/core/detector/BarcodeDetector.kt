package soup.qr.core.detector

import com.google.mlkit.vision.common.InputImage

interface BarcodeDetector {

    fun detect(inputImage: InputImage)
}
