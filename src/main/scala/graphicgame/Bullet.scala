package graphicgame

import org.scalactic.Bool

class Bullet(private var _x: Double, private var _y: Double, val level: Level, 
  val player: Player, val shotBy: BulletGenerator, val goal: Entity, val dx: Double, val dy: Double, val dist: Double) extends Entity {
    private var alive: Boolean = true
    private var target: Entity = goal
    val speed: Int = 12
    val range: Int = shotBy.shootRange

    def x: Double = _x
    def y: Double = _y
    def width: Double = 1
    def height: Double = 0.5
    def die(): Unit = alive = false
    def fromPlayer: Player = player

    def update(delay: Double): Unit = {
      this.move(dx/dist*speed*delay, dy/dist*speed*delay)

      /*if (dir == 0) this.move(0, -speed * delay)
      if (dir == 1) this.move(0, speed * delay)
      if (dir == 2) this.move(-speed * delay, 0)
      if (dir == 3) this.move(speed * delay, 0)*/
    }

    def move(dx: Double, dy: Double): Unit = {
      _x += dx
      _y += dy
    }

    def postCheck(): Unit = {
      if(_x < 0 || _x > level.maze.width || _y < 0 || _y > level.maze.height) {
        this.die()
      }

      /*val touching = level.enemies.map(e => Entity.intersect(this, e))
      if(!touching.isEmpty) {
        if(touching.reduce(_ || _)) alive = false
      }*/
    }

    def stillHere(): Boolean = alive

    def buildPassable(): PassableEntity = new PassableEntity(x, y, width, height, EntityType.Bullet)
}
