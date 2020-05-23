package soup.qr.core.detector.firebase

import android.graphics.*

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
