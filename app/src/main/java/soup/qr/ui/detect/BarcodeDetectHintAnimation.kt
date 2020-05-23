package soup.qr.ui.detect

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import soup.qr.R
import soup.qr.databinding.DetectBinding

class BarcodeDetectHintAnimation(private val binding: DetectBinding) {

    private val maxTranslation: Float =
        binding.root.resources.getDimension(R.dimen.qr_hint_max_translation)

    private val hintAnimator: Animator =
        configureAnimator(ValueAnimator.ofFloat(1f, 0.3f), 1000) { animation ->
            binding.run {
                val alpha = lerp(1f, 0.3f, animation.animatedFraction)
                qrHint.alpha = alpha
                qrHint1.alpha = alpha
                qrHint2.alpha = alpha
                qrHint3.alpha = alpha
                qrHint4.alpha = alpha

                val offset = lerp(0f, maxTranslation, animation.animatedFraction)
                qrHint1.translationX = -offset
                qrHint2.translationX = +offset
                qrHint3.translationX = -offset
                qrHint4.translationX = +offset
                qrHint1.translationY = -offset
                qrHint2.translationY = -offset
                qrHint3.translationY = +offset
                qrHint4.translationY = +offset
            }
        }

    private fun configureAnimator(
        animator: ValueAnimator,
        duration: Long,
        onAnimationUpdate: (animation: ValueAnimator) -> Unit
    ): ValueAnimator {
        animator.duration = duration
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener(ValueAnimator.AnimatorUpdateListener(onAnimationUpdate))
        return animator
    }

    fun start() {
        hintAnimator.start()
    }

    fun stop() {
        hintAnimator.pause()
    }

    private fun lerp(start: Float, stop: Float, amount: Float): Float {
        return (1 - amount) * start + amount * stop
    }
}
