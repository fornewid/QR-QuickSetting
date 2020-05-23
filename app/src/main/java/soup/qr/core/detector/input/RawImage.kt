package soup.qr.core.detector.input

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun bitmapOf(image: Image, rotationDegrees: Int): Bitmap {
    fun rotation(): Int {
        return when (rotationDegrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }
    return FirebaseVisionImage.fromMediaImage(image, rotation()).bitmap
}

suspend fun bitmapOf(context: Context, fileUri: Uri): Bitmap {
    return withContext(Dispatchers.IO) {
        FirebaseVisionImage.fromFilePath(context, fileUri).bitmap
    }
}
