package graphicgame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.animation.AnimationTimer
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scalafx.scene.input.MouseEvent

/**
 * This is a stub for the graphical game.
 */
object Main extends JFXApp {
  val odds = 0.3
	val entities = Seq[Entity]()
	val level = new Level(RandomMaze(3, false, 10, 10, 0.5), entities)
  val player = new Player(29, 29, level, "Player1")
  val generator = new Generator(2, 2, level)
	val canvas = new Canvas(960, 960)
	val gc = canvas.graphicsContext2D
	val renderer = new Renderer2D(gc, 30)

	level += player
	level += generator

	stage = new JFXApp.PrimaryStage {
		title = "Graphic Game" // Change this to match the theme of your game.
		scene = new Scene(960, 960) {
			content = canvas

			onKeyPressed = (ke: KeyEvent) => player.keyPressed(InputData.codeToInt(ke.code))
      onKeyReleased = (ke: KeyEvent) => player.keyReleased(InputData.codeToInt(ke.code))
      onMouseClicked = (me: MouseEvent) => {
				player.mouseClicked(renderer.pixelsToBlocksX(me.x).toInt, renderer.pixelsToBlocksY(me.y).toInt)
			}

      var lastTime = -1L
			val timer = AnimationTimer(time => {
				if (lastTime >= 0) {
					val delay = (time - lastTime)/1e9
					level.updateAll(delay)
				}
				lastTime = time
			})
			timer.start()
		}
	}
}
