package com.example.customviewexample

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class BarsWaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barCount = 7
    private val barWidth = 60f
    private val barSpacing = 40f
    private val minBarHeight = 80f
    private val maxBarHeight = 350f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF6200EE.toInt()
        style = Paint.Style.FILL
    }
    private val barHeights = FloatArray(barCount) { minBarHeight }
    private val animators = mutableListOf<ValueAnimator>()

    var isRunning = true
        private set

    fun stopAnimation() {
        isRunning = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animators.forEach { it.pause() }
        } else {
            animators.forEach { it.cancel() }
        }
    }

    fun startAnimation() {
        if (isRunning) return
        isRunning = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animators.forEach { it.resume() }
        } else {
            // Dla starszych API trzeba utworzyć animacje od nowa
            startAnimations()
        }
    }

    init {
        startAnimations()
    }

    private fun startAnimations() {
        animators.clear()
        for (i in 0 until barCount) {
            val animator = ValueAnimator.ofFloat(minBarHeight, maxBarHeight).apply {
                duration = 500 + Random.nextLong(0, 500)
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                startDelay = Random.nextLong(0, 300)
                addUpdateListener {
                    barHeights[i] = it.animatedValue as Float
                    invalidate()
                }
            }
            animator.start()
            animators.add(animator)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val totalWidth = barCount * barWidth + (barCount - 1) * barSpacing
        val startX = (width - totalWidth) / 2f
        val baseY = height * 0.8f

        for (i in 0 until barCount) {
            val left = startX + i * (barWidth + barSpacing)
            val top = baseY - barHeights[i]
            val right = left + barWidth
            val bottom = baseY
            canvas.drawRect(left, top, right, bottom, paint)
        }
    }
}