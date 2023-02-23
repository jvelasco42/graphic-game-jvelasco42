package graphicgame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.canvas.Canvas
import scalafx.scene.Scene
import scalafx.scene.input.KeyEvent
import java.net.Socket
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalafx.scene.input.MouseEvent
import scalafx.application.Platform

object Client extends JFXApp {
  val canvas = new Canvas(960, 960)
  val gc = canvas.graphicsContext2D
  val renderer = new Renderer2D(gc, 30)

    
  val sock = new Socket("localhost", 8000)
  val oos = new ObjectOutputStream(sock.getOutputStream())
  val ois = new ObjectInputStream(sock.getInputStream())

  Future {
    while(true) {
      val info = ois.readObject() match {
        case i:UpdateInfo => i
        case _ => println("Error with update info sent to client")
        null
      }
      Platform.runLater(renderer.render(info.level, 15.5, 15.5, info.health, info.money, info.shot, info.target, 
        info.name, info.numAlive, info.winner))
      
    }
  }

  stage = new JFXApp.PrimaryStage {
    title = "Tower Defense"
    scene = new Scene(960,960) {
      content = canvas

      onMouseClicked = (me: MouseEvent) => {
        oos.writeInt(InputData.MouseClicked)
        oos.writeInt(renderer.pixelsToBlocksX(me.x).toInt)
        oos.writeInt(renderer.pixelsToBlocksY(me.y).toInt)
        oos.flush()
      }
      onKeyPressed = (ke: KeyEvent) => {
        oos.writeInt(InputData.KeyPressed)
        oos.writeInt(InputData.codeToInt(ke.code))
        oos.flush()
      }
      onKeyReleased = (ke: KeyEvent) => {
        oos.writeInt(InputData.KeyReleased)
        oos.writeInt(InputData.codeToInt(ke.code))
        oos.flush()
      }
    }
  }

}