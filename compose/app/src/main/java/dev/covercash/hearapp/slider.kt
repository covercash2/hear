package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.border.DrawBorder
import androidx.ui.graphics.Canvas
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.Container
import androidx.ui.tooling.preview.Preview

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

@Preview
@Composable
fun SliderPreview() {
    val state = Value(.7f)

    Slider(state = state) { /* on change*/ }
}

@Composable
fun Slider(
    state: Value,
    height: Dp = 16.dp,
    onChange: ((value: Float) -> Unit)? = null
) {
    val inputHandler =
        SliderPointerInputHandler { percent ->
            state.percent = percent
            onChange?.invoke(state.value)
        }

    PointerInputWrapper(
        pointerInputHandler = inputHandler
    ) {
        Container(
            height = height,
            expanded = true,
            alignment = Alignment.TopLeft
        ) {
            DrawBorder(shape = RectangleShape, border = Border(Color.Red, 2.dp))

            Draw { canvas: Canvas, parentSize ->
                val parentRect = parentSize.toRect()
                val rect = parentRect.copy(
                    right = (parentRect.right - parentRect.left) * state.percent
                )
                inputHandler.bounds = Pair(parentRect.left, parentRect.right)
                val paint = Paint().apply {
                    this.color = Color.Blue
                }
                canvas.drawRect(rect, paint)
            }
        }
    }
}

fun <T : Any> T.logTag(): String = this.javaClass.simpleName

/**
 * Calculate the percent distance between two points on a line.
 *
 * @param min the lesser value
 * @param max the greater value
 * @return the percent distance between min and max
 */
fun Px.percentage(min: Px, max: Px): Float =
    this.minus(min).div(max.minus(min))

fun Float.percentage(min: Float, max: Float): Float =
    this.minus(min).div(max.minus(min))

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
