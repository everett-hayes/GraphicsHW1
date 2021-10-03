import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.math.*

class DonutGeometry(
    val gl : WebGL2RenderingContext,
    val x : Float,
    val y : Float,
    val r1 : Float,
    val r2 : Float
    ) {

    // 360 triangle circle
    // 1/3 of those vertices will be the origin
    // 2/3 will be the outer points (1/2 of which are unique)
    val vertexBuffer = gl.createBuffer()
    init {

        val points: MutableList<Float> = ArrayList()

        for (i in 0..360) {

            val radian : Double = i * (PI / 180)

            val innerCircleX : Float = (r1 * cos(radian) + x).toFloat()
            val innerCircleY : Float = (r1 * sin(radian) + y).toFloat()

            points.add(innerCircleX); points.add(innerCircleY); points.add(0f) // inner point
        }

        for (i in 0..360) {

            val radian : Double = i * (PI / 180)

            val outerCircleX : Float = (r2 * cos(radian) + x).toFloat()
            val outerCircleY : Float = (r2 * sin(radian) + y).toFloat()

            points.add(outerCircleX); points.add(outerCircleY); points.add(0f) // inner point
        }

        val points_array: Array<Float> = points.toTypedArray()

        gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer) 
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(points_array), GL.STATIC_DRAW)
    }

    val indexBuffer = gl.createBuffer()
    init {

        gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)

        val triangles: MutableList<Short> = ArrayList()

        for (i in 0..360) {

            val forward : Short = (i + 360).toShort()
            var moreForward : Short = (i + 375).toShort()

            // wrap around
            if (moreForward > 720) {
                moreForward = (moreForward - 360).toShort()
            }

            triangles.add(i.toShort()); triangles.add(forward); triangles.add(moreForward);
        }

        val triangles_array: Array<Short> = triangles.toTypedArray()

        gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,
        Uint16Array(triangles_array),
        GL.STATIC_DRAW)
    }

    val inputLayout = gl.createVertexArray()
    init {
        gl.bindVertexArray(inputLayout)

        gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
        gl.enableVertexAttribArray(0)
        gl.vertexAttribPointer(0,
            3, GL.FLOAT, //< three pieces of float
            false, //< do not normalize (make unit length)
            0, //< tightly packed
            0 //< data starts at array start
        )
        gl.bindVertexArray(null)
    }

    fun draw() {

        gl.bindVertexArray(inputLayout)
        gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)  
        gl.drawElements(GL.TRIANGLES, 1080, GL.UNSIGNED_SHORT, 0)
    }
}