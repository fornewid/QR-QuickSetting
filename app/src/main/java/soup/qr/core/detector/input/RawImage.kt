package soup.qr.core.detector.input

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

interface RawImage {
    fun toBitmap(): Bitmap
}

fun rawImageOf(image: Image, rotationDegrees: Int): RawImage {
    return object : RawImage {

        override fun toBitmap(): Bitmap {
            return FirebaseVisionImage.fromMediaImage(image, rotation()).bitmap
        }

        private fun rotation(): Int {
            return when (rotationDegrees) {
                0 -> FirebaseVisionImageMetadata.ROTATION_0
                90 -> FirebaseVisionImageMetadata.ROTATION_90
                180 -> FirebaseVisionImageMetadata.ROTATION_180
                270 -> FirebaseVisionImageMetadata.ROTATION_270
                else -> FirebaseVisionImageMetadata.ROTATION_0
            }
        }
    }
}

fun rawImageOf(context: Context, fileUri: Uri): RawImage {
    return object : RawImage {
        override fun toBitmap(): Bitmap {
            return FirebaseVisionImage.fromFilePath(context, fileUri).bitmap
        }
    }
}
