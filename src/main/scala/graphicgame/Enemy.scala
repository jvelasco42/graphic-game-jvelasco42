package graphicgame

import collection.mutable.Set
import adt.ArrayQueue

class Enemy(private var _x: Double, private var _y: Double, val level: Level, private var health: Int, val target: Int) extends Entity {
  private var dir: Int = 0
  private var alive: Boolean = true
  private var player: Player = null
  val speed: Int = 8
  val offsets = Array((-1, 0), (1, 0), (0, -1), (0, 1))
  

  def x: Double = _x
  def y: Double = _y
  def width: Double = 0.75
  def height: Double = 1
  def die(): Unit = alive = false

  if(target == 1) player = level.players.reverse.head
  else if(target == 2) player = level.players.reverse.tail.head
  else if(target == 3) player = level.players.head
    
  def update(delay: Double): Unit = {
    if ((_x - player.x).abs + (_y - player.y).abs > 2) {
      val up = shortestPath(_x, _y-1, player.x, player.y)
      val down = shortestPath(_x, _y+1, player.x, player.y)
      val left = shortestPath(_x-1, _y, player.x, player.y)
      val right = shortestPath(_x+1, _y, player.x, player.y)

      val possibleDirs = Array(up, down, left, right)

      if(possibleDirs.min == up) this.move(0, -speed * delay)
      if(possibleDirs.min == down) this.move(0, speed * delay)
      if(possibleDirs.min == left) this.move(-speed * delay, 0)
      if(possibleDirs.min == right) this.move(speed * delay, 0)
    } 
    else {
      val dx = player.x - _x
      val dy = player.y - _y
      val dist = math.sqrt(dx*dx + dy*dy)
      this.move(dx/dist*speed*delay, dy/dist*speed*delay)
    }

    /*case 4 => this.move(-speed * delay, -speed * delay)
    case 5 => this.move(speed * delay, -speed * delay)
    case 6 => this.move(-speed * delay, speed * delay)
    case 7 => this.move(speed * delay, speed * delay)*/
  }

  def move(dx: Double, dy: Double): Unit = {
    if(level.maze.isClear(_x + dx, _y + dy, width, height, this)) {
      _x += dx
      _y += dy
    }
  }
  
  def shortestPath(sx: Double, sy: Double, ex: Double, ey: Double): Int = {
    if(level.maze.isClear(sx, sy, width, height, this)) {
      val queue = new ArrayQueue[(Double, Double, Int)]()
      val visited = Set[(Double, Double)]((sx, sy))
      queue.enqueue((sx, sy, 0))
      while (!queue.isEmpty) {
        val (x, y, steps) = queue.dequeue()
        for ((offsetx, offsety) <- offsets) {
          val nx = x + offsetx
          val ny = y + offsety
          if (level.maze.isClear(nx, ny, width, height, this) && !visited((nx, ny))) {
            if ((nx - ex).abs < 0.6 && (ny - ey).abs < 0.6) return steps + 1
            queue.enqueue((nx, ny, steps + 1))
            visited += nx -> ny
          }
        }
      }
      1000000000
    } else 1000000000
  }

  def postCheck(): Unit = {
    val bTouching = level.bullets.find(e => Entity.intersect(this, e))
    val mTouching = level.missiles.find(e => Entity.intersect(this, e))
    if(bTouching != None) {
      bTouching.foreach(e => e.die())
      health -= 1
    }
    if(mTouching != None) {
      mTouching.foreach(e => e.die())
      health -= 2
    }
    if(health <= 0) {
      this.die()
      bTouching.foreach(e => e.fromPlayer.getMoney(1))
      mTouching.foreach(e => e.fromPlayer.getMoney(1))
    }
  }

  def stillHere(): Boolean = alive

  def buildPassable(): PassableEntity = new PassableEntity(x, y, width, height, EntityType.Enemy)
}