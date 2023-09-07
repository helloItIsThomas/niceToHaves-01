import demos.classes.Animation
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.colorBuffer
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.color.presets.ORANGE_RED
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.IntVector2
import org.openrndr.math.map
import java.io.File

fun main() = application {
    configure {
        width = 608
        height = 342
        hideWindowDecorations = true
        windowAlwaysOnTop = true
        position = IntVector2(1285,110)
//        windowTransparent = true
    }

    oliveProgram {
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

        extend {
            GLOBAL_speed = frameCount * 0.03
            animArr.forEachIndexed { i, a ->
                a((i * 0.3 + GLOBAL_speed))
            }

            drawer.clear(ColorRGBa.BLACK)
            if (frameCount == 0) {
                fadeStartTime = seconds
            }
            val timeElapsed = seconds - fadeStartTime
            animArr.forEach { a->

            }


            val printMe0 = animArr[0].introSlider0
            println("animArr[0].introSlider0:   $printMe0")
            println("timeElapsed:   $timeElapsed")

//            drawer.fill = ColorRGBa.BLACK.opac®ify((frameCount*0.005).map(
            drawer.fill = ColorRGBa.BLUE.opacify((animArr[0].introSlider0).map(
                1.0,
                0.0,
                0.0,
                1.0)
            )
            drawer.rectangle(drawer.bounds)
        }
    }
}
