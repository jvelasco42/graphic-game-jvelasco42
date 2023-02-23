package graphicgame

import scalafx.scene.input.KeyCode
import scalafx.scene.input.MouseEvent

class Player(private var _x: Double, private var _y: Double, val level: Level, playerName: String) extends Entity {
  private var keysHeld = Set[KeyCode]()
  private var clicked: Boolean = false
  private var mouseX: Int = 0
  private var mouseY: Int = 0
  private var health: Int = 20
  private var money: Int = 10
  private var range: Int = 10
  private var alive: Boolean = true
  private var shoot: Boolean = false
  private var numHeld: Boolean = false
  private var target: Int = 0
  private var shotType: String = "None" 
  private var bHeld: Boolean = false
  private var mHeld: Boolean = false
  val bulletPrice = 2
  val missilePrice = 4

  def x: Double = _x
  def y: Double = _y
  def width: Double = 1
  def height: Double = 1
  def die(): Unit = alive = false
  def name: String = playerName
  def currentHealth: Int = health
  def currentMoney: Int = money
  def currentShot: String = shotType
  def currentTarget: Int = target
  def getMoney(cash: Int): Unit = money += cash

  def update(delay: Double): Unit = {
    if(clicked == true) {
      if(math.abs(_x - mouseX) < range && math.abs(_y - mouseY) < range 
        && level.maze(mouseX, mouseY) == Wall) {
          if(shotType == "Bullet" && money >= bulletPrice) {
            val bulletGen = new BulletGenerator(mouseX + 0.5, mouseY + 0.5, level, this, target)
            level += bulletGen
            this.getMoney(-bulletPrice)
          }
          if(shotType == "Missile" && money >= missilePrice) {
            val missileGen = new MissileGenerator(mouseX + 0.5, mouseY + 0.5, level, this, target)
            level += missileGen
            this.getMoney(-missilePrice)
          }
      }
      clicked = false
    }


    if(keysHeld(KeyCode.Digit0) || keysHeld(KeyCode.Numpad0)) {
      target = 0
    }
    if(keysHeld(KeyCode.Digit1) || keysHeld(KeyCode.Numpad1)) {
      target = 1
    }
    if(keysHeld(KeyCode.Digit2) || keysHeld(KeyCode.Numpad2)) {
      target = 2
    }
    if(keysHeld(KeyCode.Digit3) || keysHeld(KeyCode.Numpad3)) {
      target = 3
    }

    if(keysHeld(KeyCode.B)) {
      if(bHeld == false) shoot = !shoot
      if(shoot == true) shotType = "Bullet"
      else shotType = "None"
      bHeld = true
    }
    else bHeld = false

    if(keysHeld(KeyCode.M)) {
      if(mHeld == false) shoot = !shoot
      if(shoot == true) shotType = "Missile"
      else shotType = "None"
      mHeld = true
    }
    else mHeld = false

    
    /*if (keysHeld(KeyCode.Down)) this.move(0, speed * delay)
    if (keysHeld(KeyCode.Left)) this.move(-speed * delay, 0)
    if (keysHeld(KeyCode.Right)) this.move(speed * delay, 0)
    if (shootDelay >= shootInterval) {
      if (keysHeld(KeyCode.W)) {
        val bullet = new Bullet(_x, _y, level, 0, this)
        level += bullet
      }
      if (keysHeld(KeyCode.S)) {
        val bullet = new Bullet(_x, _y, level, 1, this)
        level += bullet
      }
      if (keysHeld(KeyCode.A)) {
        val bullet = new Bullet(_x, _y, level, 2, this)
        level += bullet
      }
      if (keysHeld(KeyCode.D)) {
        val bullet = new Bullet(_x, _y, level, 3, this)
        level += bullet
      }
      shootDelay = 0.0
    }*/
  }

  def move(dx: Double, dy: Double): Unit = {
    if (level.maze.isClear(_x + dx, _y + dy, width, height, this)) {
      _x += dx
      _y += dy
    }
  }

  def keyPressed(keyCode: Int): Unit = {
    keyCode match {
      case 0 => keysHeld += KeyCode.Digit0
      case 1 => keysHeld += KeyCode.Digit1
      case 2 => keysHeld += KeyCode.Digit2
      case 3 => keysHeld += KeyCode.Digit3
      case 4 => keysHeld += KeyCode.B
      case 5 => keysHeld += KeyCode.M
      case _ => 
    } 
  }
  def keyReleased(keyCode: Int): Unit = {
    keyCode match {
      case 0 => keysHeld -= KeyCode.Digit0
      case 1 => keysHeld -= KeyCode.Digit1
      case 2 => keysHeld -= KeyCode.Digit2
      case 3 => keysHeld -= KeyCode.Digit3
      case 4 => keysHeld -= KeyCode.B
      case 5 => keysHeld -= KeyCode.M
      case _ => 
    } 
  }
  def mouseClicked(mx: Int, my: Int): Unit = {
    clicked = true
    mouseX = mx
    mouseY = my
  }

  def postCheck(): Unit = {
    val touching = level.enemies.find(e => Entity.intersect(this, e))
    if (touching != None) {
      health -= 1
      touching.foreach(e => e.die())
    }
    if(health == 0) this.die()
  }

  def stillHere(): Boolean = alive

  def buildPassable(): PassableEntity = new PassableEntity(x, y, width, height, EntityType.Player)
}
