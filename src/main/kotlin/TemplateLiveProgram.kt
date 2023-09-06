

import demos.classes.Animation
import kotlinx.coroutines.DelicateCoroutinesApi
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.noise.random
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.mix
import org.openrndr.writer
import java.awt.SystemColor.window
import java.io.File
import kotlin.system.measureTimeMillis


@OptIn(DelicateCoroutinesApi::class)
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
        var palette = listOf(ColorRGBa.fromHex(0xF1934B), ColorRGBa.fromHex(0x0E8847), ColorRGBa.fromHex(0xD73E1C), ColorRGBa.fromHex(0xF4ECDF), ColorRGBa.fromHex(0x552F20))
        val animation = Animation()
        val loopDelay = 3.0
        val message = "hello"
        animation.loadFromJson(File("data/keyframes/keyframes-0.json"))
        val scale: DoubleArray = typeScale(3, 100.0, 3)
        val typeFace: Pair<List<FontMap>, List<FontImageMap>> = defaultTypeSetup(scale, listOf("reg", "reg", "bold"))
        var rad = 10.0
        val animArr = mutableListOf<Animation>()
        val randNums = mutableListOf<Double>()
        val charArr = message.toCharArray()
        charArr.forEach { e ->
            animArr.add(Animation())
            randNums.add(random(0.0, 1.0))
        }
        animArr.forEach { a ->
            a.loadFromJson(File("data/keyframes/keyframes-0.json"))
        }

        val rt = renderTarget(width, height) {
            colorBuffer()
        }



        extend {
            animArr.forEachIndexed { i, a ->
                a((randNums[i] * 0.3 + frameCount * 0.02) % loopDelay)
            }
            drawer.clear(ColorRGBa.TRANSPARENT)
//            drawer.fill = ColorRGBa.WHITE
            drawer.circle(20.0, 50.0, rad + 20.0)

            drawer.isolatedWithTarget(rt) {
                drawer.clear(ColorRGBa.TRANSPARENT)
                drawer.fill = ColorRGBa.GREEN
                drawer.stroke = null
                drawer.fontMap = typeFace.second[0]
                drawer.text("H", 10.0, 70.0)
            }

            writer {
                typeFace.first.forEachIndexed { i, e ->
                    drawer.fontMap = e
                    charArr.forEachIndexed { ii, ee ->
                        drawer.pushTransforms()
                        drawer.translate(0.0, animArr[ii].pathSlider * 20.0 + 70.0)
                        text(ee.toString())
                        drawer.popTransforms()
                    }
                }
            }
        }
    }
}