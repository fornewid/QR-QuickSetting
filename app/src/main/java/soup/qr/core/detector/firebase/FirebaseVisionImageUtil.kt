package soup.qr.core.detector.firebase

import android.graphics.*
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import soup.qr.core.detector.input.RawImage

fun RawImage.toBitmap(): Bitmap {
    return FirebaseVisionImage.fromMediaImage(image, rotation()).bitmap
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

fun Bitmap.inverted(): Bitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                set(
                    floatArrayOf(
                        -1f, 0f, 0f, 0f, 255f,
                        0f, -1f, 0f, 0f, 255f,
                        0f, 0f, -1f, 0f, 255f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                preConcat(ColorMatrix().apply {
                    setSaturation(0f)
                })
            })
        }
        Canvas(it).drawBitmap(this, 0f, 0f, paint)
    }
}
