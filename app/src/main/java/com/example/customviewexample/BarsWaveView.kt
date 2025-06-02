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

    var barCount: Int
    var barWidth: Float
    var barSpacing: Float
    var minBarHeight: Float
    var maxBarHeight: Float
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF6200EE.toInt() // kolor na stałe
        style = Paint.Style.FILL
    }

    private lateinit var barHeights: FloatArray
    private val animators = mutableListOf<ValueAnimator>()

    var isRunning = false
        private set

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarsWaveView)
        barCount = typedArray.getInt(R.styleable.BarsWaveView_barCount, 7)
        barWidth = typedArray.getDimension(R.styleable.BarsWaveView_barWidth, 60f)
        barSpacing = typedArray.getDimension(R.styleable.BarsWaveView_barSpacing, 40f)
        minBarHeight = typedArray.getDimension(R.styleable.BarsWaveView_minBarHeight, 80f)
        maxBarHeight = typedArray.getDimension(R.styleable.BarsWaveView_maxBarHeight, 350f)
        typedArray.recycle()

        barHeights = FloatArray(barCount) { minBarHeight }
    }

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
        if (animators.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startAnimations()
        } else {
            animators.forEach { it.resume() }
        }
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