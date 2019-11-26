package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.engine.geometry.*
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.border.DrawBorder
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.*
import androidx.ui.layout.Container
import androidx.ui.tooling.preview.Preview

@Composable
fun Slider(
    state: Value,
    height: Dp = 24.dp,
    paint: Paint = Paint().apply { color = Color.Blue },
    onChange: ((value: Float) -> Unit)? = null
) {
    val input =
        SliderPointerInputHandler { percent ->
            state.percent = percent
            onChange?.invoke(state.value)
        }

    PointerInputWrapper(
        pointerInputHandler = input
    ) {
        Container(
            height = height,
            expanded = true,
            alignment = Alignment.TopLeft
        ) {
            Draw { canvas: Canvas, parentSize ->
                val parentRect = parentSize.toRect()
                val progressRect = parentRect.copy(
                    right = (parentRect.right - parentRect.left) * state.percent
                )
                val progressLocation = Offset(
                    progressRect.right,
                    parentRect.height / 2f
                )
                // TODO memo?
                input.bounds = Pair(parentRect.left, parentRect.right)
                // TODO parameters
                val fullLinePaint = Paint().apply {
                    color = Color.LightGray
                }

                // draw full line
                canvas.drawInnerLine(parentRect, fullLinePaint)
                // draw progress line
                canvas.drawInnerLine(progressRect, paint)

                canvas.drawCircle(
                    progressLocation,
                    parentRect.height.times(.25f),
                    paint
                )
            }

        }
    }
}

@Model
data class Value(
    var value: Float,
    val min: Float = 0f,
    val max: Float = 1f
) {
    var percent: Float
        get() =
            value.minus(min).div(max.minus(min))
        set(newPercent) {
            value = newPercent
                .coerceIn(0f, 1f)
                .times(max.minus(min)).plus(min)
        }
}

class SliderPointerInputHandler(
    val onChange: (percent: Float) -> Unit
) : PointerInputHandler {
    // left and right
    var bounds: Pair<Float, Float>? = null

    override fun invoke(
        p1: List<PointerInputChange>,
        p2: PointerEventPass,
        p3: IntPxSize
    ): List<PointerInputChange> {
        bounds?.let { (left, right) ->
            p1.filter {
                it.current.down
            }.mapNotNull {
                it.current.position
            }.map {
                it.x.value
            }.map {
                onChange(it.percentage(left, right))
            }
        }

        return p1
    }
}

@Preview
@Composable
fun SliderPreview() {
    val state = Value(.7f)

    Slider(state = state) { /* on change*/ }
}

/**
 * Draw a line through the center of a parent rectangle.
 */
private fun Canvas.drawInnerLine(parent: Rect, paint: Paint) {
    val margin = parent.height * .4f
    val rect = parent.copy(
        top = parent.top + margin,
        bottom = parent.bottom - margin
    )
    val radius = Radius.circular(90f)

    drawRRect(RRect(rect, radius), paint)
}

fun <T : Any> T.logTag(): String = this.javaClass.simpleName

/**
 * Calculate the percent distance between two points on a line.
 *
 * @param min the lesser value
 * @param max the greater value
 * @return the percent distance between min and max
 */
fun Float.percentage(min: Float, max: Float): Float =
    this.minus(min).div(max.minus(min))
