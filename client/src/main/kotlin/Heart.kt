import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.math.*

class HeartGeometry(
    val gl : WebGL2RenderingContext,
    val x : Float,
    val y : Float,
    val r : Float
    ) {

    var count : Int = 0

    val vertexBuffer = gl.createBuffer()
    init {

        val points: MutableList<Float> = ArrayList()

        points.add(x); points.add(y); points.add(0f) // origin

        var curr : Float = 0f

        while (curr < (2*PI)) {

            val currentX : Float = r * (16*(sin(curr).pow(3)))
            val currentY : Float = r * (13*cos(curr) - 5*cos(2*curr) - 2*cos(3*curr) - cos(4*curr))
            count += 1

            points.add(currentX); points.add(currentY); points.add(0f) // origin
            curr = (curr + .02).toFloat()
        }

        val points_array: Array<Float> = points.toTypedArray()

        gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer) 
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(points_array), GL.STATIC_DRAW)
    }

    val indexBuffer = gl.createBuffer()
    init {

        gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)

        val triangles: MutableList<Short> = ArrayList()


        for (i in 1..count) {
            triangles.add(0); triangles.add(i.toShort()); triangles.add((i+1).toShort());
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
        gl.drawElements(GL.TRIANGLES, (count*3)-1, GL.UNSIGNED_SHORT, 0)
    }
}