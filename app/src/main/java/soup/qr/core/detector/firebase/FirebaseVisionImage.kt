package soup.qr.core.detector.firebase

import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import soup.qr.core.detector.input.RawImage

object FirebaseVisionImage {

    fun from(rawImage: RawImage): FirebaseVisionImage = rawImage.run {
        FirebaseVisionImage.fromMediaImage(image, rotation())
    }

    private fun RawImage.rotation(): Int {
        return when (rotationDegrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }
}
