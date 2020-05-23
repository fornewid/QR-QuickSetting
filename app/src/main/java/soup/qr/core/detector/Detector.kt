package soup.qr.core.detector

import android.graphics.Bitmap

interface Detector<T: Any> {

    suspend fun detect(bitmap: Bitmap): T?

    fun isInDetecting(): Boolean
}
