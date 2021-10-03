import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.math.*

class TriangleGeometry(
    val gl : WebGL2RenderingContext,
    val x : Float,
    val y : Float,
    val r : Float
  ) {

  val vertexBuffer = gl.createBuffer()
  init {

    val points: MutableList<Float> = ArrayList()

    for (i in 0..2) {

      val oop : Float = (((2 * PI) / 3) * i).toFloat();

      val currentX : Float = (r * cos(oop) + x).toFloat()
      val currentY : Float = (r * sin(oop) + y).toFloat()

      points.add(currentX); points.add(currentY); points.add(0f) 
    }

    val points_array: Array<Float> = points.toTypedArray()

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, Float32Array(points_array), GL.STATIC_DRAW)
  }

  val indexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,
      Uint16Array( arrayOf<Short>(
        0, 1, 2
      )),
      GL.STATIC_DRAW)
  }

  val inputLayout = gl.createVertexArray() //#VertexArray# OpenGL dictionary:; vertex array object (VAO) is input layout
  init{
    gl.bindVertexArray(inputLayout)

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0, //#0# this explains how attribute 0 can be found in the vertex buffer
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
    gl.drawElements(GL.TRIANGLES, 3, GL.UNSIGNED_SHORT, 0) //#3# pipeline is all set up, draw three indices worth of geometry
  }

}
