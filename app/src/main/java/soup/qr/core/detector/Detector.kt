package soup.qr.core.detector

import soup.qr.core.detector.input.RawImage

interface Detector {

    fun detect(image: RawImage)

    fun isInDetecting(): Boolean
}
