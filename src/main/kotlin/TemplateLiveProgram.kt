import demos.classes.Animation
import org.openrndr.KEY_ESCAPE
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.colorBuffer
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.color.presets.ANTIQUE_WHITE
import org.openrndr.extra.color.presets.ORANGE_RED
import org.openrndr.extra.color.presets.PALE_GREEN
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.jumpfill.fx.OuterGlow
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extra.shapes.RoundedRectangle
import org.openrndr.extra.shapes.roundedRectangle
import org.openrndr.math.IntVector2
import org.openrndr.math.map
import org.openrndr.shape.Rectangle
import java.io.File
import kotlin.math.cos

fun main() = application {
    configure {
        width = 608
        height = 342
        hideWindowDecorations = true
        windowAlwaysOnTop = true
        position = IntVector2(1285,110)
        windowTransparent = true
    }

    oliveProgram {

        var frameCounter = 0.0
        var exitRequested = false


        keyboard.keyDown.listen { event ->
            if (event.key == KEY_ESCAPE) {
                exitRequested = true
            }
        }

        val message = "hello"
        val animArr = mutableListOf<Animation>()
        val charArr = message.toCharArray()

        charArr.forEach { _ ->
            val animation = Animation()
            animation.loadFromJson(File("data/keyframes/keyframes-0.json"))
            animArr.add(animation)
        }

        val rt = renderTarget(width, height) {
            colorBuffer()
        }

        var fadeStartTime = 0.0
        var fadeDuration = 1.0
        var waitDuration = 1.0
        var GLOBAL_speed: Double
        val loopDelay = 1.0

        var myRectangle: Rectangle
        var plugMeIn: Double

        val c = compose {
            layer {
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.circle(width / 2.0, height / 2.0, 200.0)
                }


                post(OuterGlow()) {
                    this.width = (cos(seconds) * 0.5 + 0.5) * 100.0
                }
            }
        }


        extend {
//                        GLOBAL_speed = frameCount * 0.0333
                        GLOBAL_speed = frameCount * 0.0222
//                        GLOBAL_speed = frameCount * 0.00922
//                        GLOBAL_speed = frameCount * 0.0088
//            GLOBAL_speed = frameCount * 0.00333
            //            GLOBAL_speed = frameCount * 0.033
            val mappedVal = (animArr[0].introSlider0).map(
                1.0,
                0.0,
                0.0,
                1.0
            )
            animArr.forEachIndexed { i, a ->
                if (i != 1) a((i * 0.3 + GLOBAL_speed))
            }
            plugMeIn = mappedVal

            if (exitRequested) {
                frameCounter += 0.01
//                animArr[1]((1 * 0.3 + frameCounter))
//                plugMeIn = animArr[1].pathSlider
                plugMeIn = frameCounter.map(
                    0.0,
                    0.333,
                    1.0,
                    0.0
                )
                if (frameCounter >= 1) {
                    application.exit()
                }
            }

            drawer.clear(ColorRGBa.TRANSPARENT)
            if (frameCount == 0) {
                fadeStartTime = seconds
            }
            val timeElapsed = seconds - fadeStartTime



//            drawer.fill = ColorRGBa.BLACK.opacify((frameCount*0.005).map(
            drawer.fill = null
//            drawer.stroke = ColorRGBa.ANTIQUE_WHITE.opacify((animArr[0].introSlider0).map(


            drawer.fill = ColorRGBa.BLACK.opacify(plugMeIn)

            myRectangle = Rectangle(
                (drawer.bounds.center.x - (drawer.bounds.width * 0.25)*plugMeIn),
                (drawer.bounds.center.y  - (drawer.bounds.height * 0.25)*plugMeIn),
                (drawer.bounds.width * 0.5)*plugMeIn,
                (drawer.bounds.height * 0.5)*plugMeIn
            )
//            drawer.stroke = null
            drawer.stroke = ColorRGBa.PALE_GREEN.opacify(plugMeIn)
//            drawer.shape(myRectangle.shape)


            drawer.roundedRectangle(
                (drawer.bounds.center.x - (drawer.bounds.width * 0.25)*plugMeIn)+ 0.1,
                (drawer.bounds.center.y  - (drawer.bounds.height * 0.25)*plugMeIn)+ 0.1,
                (drawer.bounds.width * 0.5)*plugMeIn + 0.1,
                (drawer.bounds.height * 0.5)*plugMeIn + 0.1,
                plugMeIn * 10.0
            )
//            drawer.contour(myRectangle.contour.sub(
//                1 - (plugMeIn + 0.06), 1.0
//            ))
            c.draw(drawer)
        }
    }
}
