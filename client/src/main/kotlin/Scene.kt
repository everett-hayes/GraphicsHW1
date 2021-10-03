import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.*

class Scene (
  val gl : WebGL2RenderingContext){

  val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")

  val vsTri = Shader(gl, GL.VERTEX_SHADER, "tri-vs.glsl")
  val fsTri = Shader(gl, GL.FRAGMENT_SHADER, "tri-fs.glsl")

  val solidProgram = Program(gl, vsIdle, fsSolid)
  val triProgram = Program(gl, vsTri, fsTri)

  val donutGeometry = DonutGeometry(gl, 0.0F, 0.0F, 0.35f, 0.375f)
  val heartGeometry = HeartGeometry(gl, 0.0F, 0.0F, 0.02f)
  val triangleGeometry0 = TriangleGeometry(gl, 0.45F, 0.45F, 0.2F)
  val triangleGeometry1 = TriangleGeometry(gl, -0.45F, -0.45F, 0.2F)
  val triangleGeometry2 = TriangleGeometry(gl, 0.45F, -0.45F, 0.2F)
  val triangleGeometry3 = TriangleGeometry(gl, -0.45F, 0.45F, 0.2F)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame = timeAtFirstFrame

  val avatarPosition = Vec3(0.0f, 0.0f)
  var avatarRotation = 0.0f
  var avatarRotation1 = 0.0f

  fun resize(canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>) {

    // timing stuff
    val timeAtThisFrame = Date().getTime()
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame

    gl.clearColor(0.8f, 0.91f, 0.96f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)
    gl.useProgram(solidProgram.glProgram)
    heartGeometry.draw()
    donutGeometry.draw()

    if ("w" in keysPressed) {
      avatarPosition += Vec3(0.0f, 0.5f) * dt
    }

    if ("s" in keysPressed) {
      avatarPosition += Vec3(0.0f, -0.5f) * dt
    }

    if ("a" in keysPressed) {
      avatarPosition += Vec3(-0.5f, 0.0f) * dt
    }

    if ("d" in keysPressed) {
      avatarPosition += Vec3(0.5f, 0.0f) * dt
    }

    if (avatarPosition.x > 1.0f) {
      avatarPosition.x = -1.0f
    } else if (avatarPosition.x < -1.0f) {
      avatarPosition.x = 1.0f
    }

    if (avatarPosition.y > 1.0f) {
      avatarPosition.y = -1.0f
    } else if (avatarPosition.y < -1.0f) {
      avatarPosition.y = 1.0f
    }

    avatarRotation += .01f

    if (avatarRotation > 6.28f) {
      avatarRotation = 0.0f
    }

    val modelMatrixHandle = gl.getUniformLocation(solidProgram.glProgram, "gameObject.modelMatrix")
    if (modelMatrixHandle == null) {
      console.log("oops")
    } else {
      val modelMatrix = Mat4().translate(Vec3(0.0f, 0.0f, 0.0f))
      modelMatrix.rotate(avatarRotation, Vec3(0.0f, 0.0f, 0.0f))
      modelMatrix.translate(avatarPosition)
      modelMatrix.commit(gl, modelMatrixHandle)
    }

    val timeHandle = gl.getUniformLocation(solidProgram.glProgram, "scene.time");
    gl.uniform1f(timeHandle, t);

    gl.useProgram(triProgram.glProgram)
    triangleGeometry0.draw()
    triangleGeometry1.draw()
    triangleGeometry2.draw()
    triangleGeometry3.draw()

    avatarRotation1 -= .01f

    val modelMatrixHandleTri = gl.getUniformLocation(triProgram.glProgram, "gameObject.modelMatrix")
    if (modelMatrixHandleTri == null) {
      console.log("oops")
    } else {
      val modelMatrix = Mat4().rotate(avatarRotation1, Vec3(0.0f, 0.0f, 0.0f))
      modelMatrix.commit(gl, modelMatrixHandleTri)
    }

    val timeHandle1 = gl.getUniformLocation(triProgram.glProgram, "scene.time");
    gl.uniform1f(timeHandle1, t);
  }
}
