import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.*
import org.w3c.dom.Window

class App(val canvas : HTMLCanvasElement, val overlay : HTMLDivElement) {

  val gl = (canvas.getContext("webgl2", object{val alpha = false}) ?: throw Error("Browser does not support WebGL2")) as WebGL2RenderingContext
  val scene = Scene(gl)
  var pressed = HashSet<String>()

  init {
    resize()
  }

  fun resize() {
    canvas.width = canvas.clientWidth
    canvas.height = canvas.clientHeight
    scene.resize(canvas)
  }

  @Suppress("UNUSED_PARAMETER")
  fun registerEventHandlers() {
    document.onkeydown =  { 
      event : KeyboardEvent ->
      pressed.add(event.key)
    }

    document.onkeyup = { 
      event : KeyboardEvent ->
      pressed.remove(event.key)
    }

    canvas.onmousedown = { 
      event : MouseEvent ->
      event
    }

    canvas.onmousemove = { 
      event : Event ->
      event.stopPropagation()
    }

    canvas.onmouseup = { 
      event : Event ->
      event
    }

    canvas.onmouseout = { 
      event : Event ->
      event
    }

    window.onresize = {
      event : Event ->
      resize()
    };  

    window.requestAnimationFrame {
      update()
    }
  }  

  fun update() {
    scene.update(pressed)
    window.requestAnimationFrame { 
      update() 
    }
  }
}

fun main() {
  val canvas = document.getElementById("canvas") as HTMLCanvasElement
  val overlay = document.getElementById("overlay") as HTMLDivElement
  overlay.innerHTML = """<div id="textField" style="color: #F2BAC9; font-size: 35px">WebGL by Everett</div>"""

  try{
    val app = App(canvas, overlay)
    app.registerEventHandlers()
  } catch(e : Error) {
    console.error(e.message)
  }
}