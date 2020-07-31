package soup.qr.core.detector.input

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.view.Surface
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun bitmapOf(image: Image, rotationDegrees: Int): Bitmap? {
    return InputImage.fromMediaImage(image, rotationDegrees).bitmapInternal
}

suspend fun bitmapOf(context: Context, fileUri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        runCatching { InputImage.fromFilePath(context, fileUri).bitmapInternal }.getOrNull()
    }
}
