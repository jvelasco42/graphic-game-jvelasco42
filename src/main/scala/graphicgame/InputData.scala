package graphicgame

import scalafx.scene.input.KeyCode
import scalafx.scene.input.MouseEvent

object InputData {
  val KeyPressed = 1001
  val KeyReleased = 1002
  val MouseClicked = 1003
  val Zero = 0
  val One = 1
  val Two = 2
  val Three = 3
  val Bullet = 4
  val Missile = 5

  def codeToInt: Map[KeyCode, Int] = Map(KeyCode.Digit0 -> Zero, KeyCode.Digit1 -> One, KeyCode.Digit2 -> Two, 
    KeyCode.Digit3 -> Three, KeyCode.B -> Bullet, KeyCode.M -> Missile)
}