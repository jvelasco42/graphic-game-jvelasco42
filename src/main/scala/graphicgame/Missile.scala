package graphicgame

import org.scalactic.Bool

class Missile(private var _x: Double, private var _y: Double, val level: Level, 
  val player: Player, val shotBy: MissileGenerator, val goal: Entity) extends Entity {
    private var alive: Boolean = true
    private var target: Entity = goal
    val speed: Int = 20
    val range: Int = shotBy.shootRange

    def x: Double = _x
    def y: Double = _y
    def width: Double = 1
    def height: Double = 1
    def die(): Unit = alive = false
    def fromPlayer: Player = player

    def update(delay: Double): Unit = {
      if(level.enemies.length > 0) {
        target = level.enemies.reverse.head
        // if(level.enemies.length > 1 && (math.abs(target.x - shotBy.x) > range || math.abs(target.y - shotBy.y) > range)) {
        //   target = level.enemies.tail.head
        // }
      } 
      val dx = target.x - _x
      val dy = target.y - _y
      val dist = math.sqrt(dx*dx + dy*dy)
      this.move(dx/dist*speed*delay, dy/dist*speed*delay)
    }

    def move(dx: Double, dy: Double): Unit = {
      _x += dx
      _y += dy
    }

    def postCheck(): Unit = {
      if(_x < 0 || _x > level.maze.width || _y < 0 || _y > level.maze.height || 
        math.abs(shotBy.x - _x) > range || math.abs(shotBy.y - _y) > range) {
          this.die()
      }
    }

    def stillHere(): Boolean = alive

    def buildPassable(): PassableEntity = new PassableEntity(x, y, width, height, EntityType.Missile)
}
