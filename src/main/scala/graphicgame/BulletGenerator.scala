package graphicgame

class BulletGenerator(val _x: Double, val _y: Double, val level: Level, val madeBy: Player, goal: Int) extends Entity {
  private var alive: Boolean = true 
  private var target: Entity = null
  private var inRange: Boolean = false 
  private var spawnDelay: Double  = 0.0
  val spawnInterval: Double = 0.3
  val range: Int = 5

  def x: Double = _x
  def y: Double = _y
  def width: Double = 1
  def height: Double = 1
  def die(): Unit = alive = false
  def shootRange: Int = range

  goal match {
    case 0 => target = level.generators.head
    case 1 => target = level.players.reverse.head
    case 2 => target = level.players.reverse.tail.head
    case 3 => target = level.players.head
  }
  
  def update(delay: Double): Unit = {
    spawnDelay += delay

    if((math.abs(target.x - _x) <= range && math.abs(target.y - _y) <= range)) inRange = true
    else inRange = false

    if(level.enemies.length > 0) {
      target = level.enemies.reverse.head
      // if(level.enemies.length > 1 && inRange == false) {
      //   target = level.enemies.tail.head
      // }
    }
    val dx = target.x - _x
    val dy = target.y - _y
    val dist = math.sqrt(dx*dx + dy*dy)

    if(spawnDelay >= spawnInterval && inRange == true) {
      val bullet = new Bullet(_x, _y, level, madeBy, this, target, dx, dy, dist)
      level += bullet
      spawnDelay = 0.0
    }
  }

  def postCheck(): Unit = {
  }

  def stillHere(): Boolean = alive

  def buildPassable(): PassableEntity = new PassableEntity(x, y, width, height, EntityType.BulletGenerator)
}