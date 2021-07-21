package xyz.ridsoft.hal.common

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import androidx.core.graphics.toRect
import androidx.recyclerview.widget.RecyclerView

class RoundedCornerDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val CORNER_RADIUS = 24f
    }

    private val defaultRectToClip = RectF(0f, 0f, 0f, 0f)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val rectToClip = getRectToClip(parent)

        val path = Path()
        path.addRoundRect(rectToClip, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW)

        c.clipPath(path)
    }

    private fun getRectToClip(parent: RecyclerView): RectF {
        val rectToClip = RectF(defaultRectToClip)
        val childRect = RectF()

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, childRect.toRect())

            rectToClip.left = rectToClip.left.coerceAtMost(childRect.left);
            rectToClip.top = rectToClip.top.coerceAtMost(childRect.top);
            rectToClip.right = rectToClip.right.coerceAtLeast(childRect.right);
            rectToClip.bottom = rectToClip.bottom.coerceAtLeast(childRect.bottom);
        }

        return rectToClip
    }
}