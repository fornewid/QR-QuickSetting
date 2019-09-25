package soup.qr.ui.detect

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.animation.AccelerateDecelerateInterpolator
import soup.qr.R
import soup.qr.databinding.FragmentDetectBinding

class BarcodeDetectHintAnimation(private val binding: FragmentDetectBinding) {

    private val alphaUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val alpha = animation.animatedValue as Float
        binding.run {
            qrHint.alpha = alpha
            qrHint1.alpha = alpha
            qrHint2.alpha = alpha
            qrHint3.alpha = alpha
            qrHint4.alpha = alpha
        }
    }

    private val translationUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val offset = animation.animatedValue as Float
        binding.run {
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

    private val maxTranslation: Float =
        binding.root.context.resources.getDimension(R.dimen.qr_hint_max_translation)

    private val hintAnimator: Animator = makeHintAnimator()

    private fun makeHintAnimator(): Animator {
        val alphaAnimator: ValueAnimator = configureAnimator(
            ValueAnimator.ofFloat(1f, 0.3f), 1000, alphaUpdateListener
        )
        val sizeAnimator: ValueAnimator = configureAnimator(
            ValueAnimator.ofFloat(0f, maxTranslation), 1000, translationUpdateListener
        )
        return AnimatorSet().apply {
            playTogether(alphaAnimator, sizeAnimator)
        }
    }

    private fun configureAnimator(
        animator: ValueAnimator,
        duration: Long,
        updateListener: ValueAnimator.AnimatorUpdateListener
    ): ValueAnimator {
        animator.duration = duration
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener(updateListener)
        return animator
    }

    fun start() {
        hintAnimator.start()
    }

    fun stop() {
        hintAnimator.pause()
    }

    fun onSuccess() {
        setColor(Color.WHITE)
    }

    fun onError() {
        setColor(Color.RED)
    }

    private fun setColor(color: Int) {
        val tint = ColorStateList.valueOf(color)
        binding.run {
            qrHint.imageTintList = tint
            qrHint1.imageTintList = tint
            qrHint2.imageTintList = tint
            qrHint3.imageTintList = tint
            qrHint4.imageTintList = tint
        }
    }
}
