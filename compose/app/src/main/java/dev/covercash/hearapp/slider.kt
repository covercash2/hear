package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.Vertices
import androidx.ui.core.*
import androidx.ui.engine.geometry.*
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.border.DrawBorder
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.*
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.tooling.preview.Preview

@Composable
fun Slider(
    state: Value,
    height: Dp = 24.dp,
    paint: Paint = Paint().apply { color = Color.Blue },
    backgroundPaint: Paint? = Paint().apply {
        color = Color.LightGray
        alpha = 150f
    },
    indicator: ((Canvas, Rect) -> Unit)? = null,
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
                val progressX = (parentRect.right - parentRect.left) * state.percent
                val halfHeight = parentRect.height / 2f
                // TODO memo?
                input.bounds = Pair(parentRect.left, parentRect.right)
                // TODO parameters

                // draw full line
                backgroundPaint?.let {
                    canvas.drawInnerLine(parentRect, it)
                }
                // draw progress line
                canvas.drawInnerLine(progressRect, paint)

                val indicatorBounds: Rect = parentRect.copy(
                    left = progressX - halfHeight,
                    right = progressX + halfHeight
                )

                when (indicator) {
                    null -> canvas.drawOval(
                        indicatorBounds.let {
                            it.deflate(it.height * .25f)
                        },
                        paint
                    )
                    else -> indicator(canvas, indicatorBounds)
                }
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
        pointerEvents: List<PointerInputChange>,
        p2: PointerEventPass,
        p3: IntPxSize
    ): List<PointerInputChange> {
        bounds?.let { (left, right) ->
            pointerEvents.filter { event ->
                event.current.down
            }.mapNotNull { event ->
                event.current.position
            }.map { pxPos ->
                pxPos.x.value
            }.map { xPos ->
                xPos.percentage(left, right)
            }.map { percent ->
                onChange(percent)
            }
        }

        return pointerEvents
    }
}

@Preview
@Composable
fun SliderPreview() {
    val state0 = Value(.7f)
    val state1 = Value(.4f)
    val state2 = Value(.6f)
    val state3 = Value(0.5f)
    val state4 = Value(0.8f)
    val state5 = Value(0.3f)

    val slider2Paint = colorPaint(Color.Green)
    val slider3Paint = colorPaint(Color.Cyan)
    val slider4Paint = colorPaint(Color.Magenta)
    val slider5Paint = colorPaint(Color.Black)

    Column {
        Slider(state = state0)
        Slider(
            state = state1,
            paint = colorPaint(Color.Red),
            backgroundPaint = colorPaint(Color.Black)
        )
        Slider(
            state = state2,
            paint = slider2Paint,
            indicator = { canvas, bounds ->
                canvas.drawRect(bounds, slider2Paint)
            }
        )
        Slider(
            state = state3,
            paint = slider3Paint,
            backgroundPaint = null,
            indicator = { canvas, bounds ->
                canvas.drawDoubleRoundRect(
                    RRect(bounds, Radius.circular(3f)),
                    RRect(bounds.deflate(8f), Radius.circular(3f)),
                    Paint().apply { color = Color.White }
                )
            }
        )
        Slider(
            state = state4,
            paint = slider4Paint,
            backgroundPaint = Paint().apply {
                style = PaintingStyle.stroke
            },
            indicator = { canvas, bounds ->
                canvas.drawRRect(
                    RRect(
                        bounds.copy(
                            left = bounds.getCenter().dx - 8f,
                            right = bounds.getCenter().dx + 8f
                        ),
                        radius = Radius.circular(3f)
                    ),
                    Paint().apply { color = Color.Yellow }
                )
                canvas.drawLine(
                    bounds.getTopCenter(),
                    bounds.getBottomCenter(),
                    Paint().apply { color = Color.Yellow }
                )
            }
        )
        Slider(
            state = state5,
            paint = slider5Paint,
            indicator = { _, _ -> }
        )
    }
}

private fun colorPaint(color: Color) = Paint()
    .also { it.color = color }

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
