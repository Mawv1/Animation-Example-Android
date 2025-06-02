// AnimatedPlayStopButton.kt
package com.example.customviewexample

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class AnimatedPlayStopButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isPlaying = false
    private var animProgress = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF6200EE.toInt()
        style = Paint.Style.FILL
    }
    private var animator: ValueAnimator? = null

    fun setPlaying(playing: Boolean) {
        if (isPlaying == playing) return
        isPlaying = playing
        animator?.cancel()
        animator = ValueAnimator.ofFloat(animProgress, if (isPlaying) 1f else 0f).apply {
            duration = 250
            addUpdateListener {
                animProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = Math.min(width, height) / 2f * 0.9f

        // Tło okrągłe
        canvas.drawCircle(cx, cy, radius, bgPaint)

        val size = radius * 0.7f
        val left = cx - size / 2
        val top = cy - size / 2
        val right = cx + size / 2
        val bottom = cy + size / 2

        val p = animProgress

        if (p < 1f) {
            // Rysuj dwa prostokąty (stop), morph do trójkąta
            val barWidth = size * 0.25f * (1 - p)
            val gap = size * 0.15f * (1 - p)
            val barHeight = size

            // Lewy prostokąt
            val leftBarLeft = cx - gap / 2 - barWidth
            val leftBarRight = cx - gap / 2
            val barTop = cy - barHeight / 2
            val barBottom = cy + barHeight / 2

            // Prawy prostokąt
            val rightBarLeft = cx + gap / 2
            val rightBarRight = cx + gap / 2 + barWidth

            // Morph do trójkąta
            val path = Path()
            // Lewy prostokąt morphuje do lewego wierzchołka trójkąta
            path.moveTo(
                leftBarLeft * (1 - p) + (left) * p,
                barTop * (1 - p) + (cy) * p
            )
            path.lineTo(
                leftBarRight * (1 - p) + (right) * p,
                barTop * (1 - p) + (cy - size / 2) * p
            )
            path.lineTo(
                leftBarRight * (1 - p) + (right) * p,
                barBottom * (1 - p) + (cy + size / 2) * p
            )
            path.lineTo(
                leftBarLeft * (1 - p) + (left) * p,
                barBottom * (1 - p) + (cy) * p
            )
            path.close()

            // Prawy prostokąt morphuje do prawego wierzchołka trójkąta
            val path2 = Path()
            path2.moveTo(
                rightBarLeft * (1 - p) + (left) * p,
                barTop * (1 - p) + (cy) * p
            )
            path2.lineTo(
                rightBarRight * (1 - p) + (right) * p,
                barTop * (1 - p) + (cy - size / 2) * p
            )
            path2.lineTo(
                rightBarRight * (1 - p) + (right) * p,
                barBottom * (1 - p) + (cy + size / 2) * p
            )
            path2.lineTo(
                rightBarLeft * (1 - p) + (left) * p,
                barBottom * (1 - p) + (cy) * p
            )
            path2.close()

            canvas.drawPath(path, paint)
            canvas.drawPath(path2, paint)
        } else {
            // Rysuj trójkąt (play)
            val path = Path()
            path.moveTo(left, top)
            path.lineTo(right, cy)
            path.lineTo(left, bottom)
            path.close()
            canvas.drawPath(path, paint)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}