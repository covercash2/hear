package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.engine.geometry.Rect
import androidx.ui.engine.geometry.Shape
import androidx.ui.graphics.Canvas
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.Container
import androidx.ui.tooling.preview.Preview

@Preview
@Composable
fun SliderPreview() {
    val state = SliderState(.7f)

    Slider(state = state) { /* on change*/ }
}

@Model
class SliderState(var percent: Float)

@Composable
fun TestSlider(
    name: String? = null,
    state: SliderState,
    height: Dp = 16.dp,
    onChange: ((percent: Float) -> Unit)? = null
) {
    val logTag = "TestSlider"

    val inputHandler =
        SliderPointerInputHandler { percent ->
            state.percent = percent
            onChange?.invoke(state.percent)
        }

    PointerInputWrapper(
        pointerInputHandler = inputHandler
    ) {
        Container(
            height = height,
//            expanded = true,
            alignment = Alignment.TopLeft
        ) {
            WithConstraints { constraints ->
                val left: Px = 0.px
                val right: Px = constraints.maxWidth.toPx()
                val top: Px = 0.px
                val bottom: Px = constraints.maxHeight.toPx()
                val position: Px = right.minus(left).times(state.percent)

                val n = name
                val i = constraints.hasBoundedWidth
                inputHandler.bounds = Pair(left, right)

                Draw { canvas: Canvas, parentSize ->
                    val parentRect = parentSize.toRect()
                    val n = name
                    val rect = Rect(
                        left = left.value,
                        top = top.value,
                        right = position.value,
                        bottom = bottom.value
                    )
                    val paint = Paint().apply {
                        this.color = Color.Blue
                    }
                    canvas.drawRect(rect, paint)
                }
            }
        }
    }
}

@Composable
fun Slider(
    state: SliderState,
    height: Dp = 16.dp,
    onChange: ((percent: Float) -> Unit)? = null
) {
    val logTag = "Slider"

    val inputHandler =
        SliderPointerInputHandler { percent ->
            state.percent = percent
            onChange?.invoke(state.percent)
        }

    PointerInputWrapper(
        pointerInputHandler = inputHandler
    ) {
        Container(
            height = height,
            expanded = true,
            alignment = Alignment.TopLeft
        ) {
            WithConstraints { constraints ->
                val left: Px = 0.px
                val right: Px = constraints.maxWidth.toPx()
                val top: Px = 0.px
                val bottom: Px = constraints.maxHeight.toPx()
                val position: Px = right.minus(left).times(state.percent)

                inputHandler.bounds = Pair(left, right)

                Draw { canvas: Canvas, _ ->
                    val rect = Rect(
                        left = left.value,
                        top = top.value,
                        right = position.value,
                        bottom = bottom.value
                    )
                    val paint = Paint().apply {
                        this.color = Color.Blue
                    }
                    canvas.drawRect(rect, paint)
                }
            }
        }
    }
}

fun <T: Any> T.logTag(): String = this.javaClass.simpleName

/**
 * Calculate the percent distance between two points on a line.
 *
 * @param min the lesser value
 * @param max the greater value
 * @return the percent distance between min and max
 */
fun Px.percentage(min: Px, max: Px): Float =
    this.minus(min).div(max.minus(min))

class SliderPointerInputHandler(
    val onChange: (percent: Float) -> Unit
) : PointerInputHandler {
    // left and right
    var bounds: Pair<Px, Px>? = null

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
                onChange(it.x.percentage(left, right))
            }
        }

        return p1
    }
}
