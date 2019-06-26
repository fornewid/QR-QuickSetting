package soup.qr.detector

import soup.qr.detector.input.RawImage

interface Detector {

    fun detect(image: RawImage)

    fun isInDetecting(): Boolean
}
