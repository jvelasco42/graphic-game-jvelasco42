package graphicgame

class Generator(private var _x: Double, private var _y: Double, val level: Level) extends Entity {
  private var alive: Boolean = true
  private var health: Int = 4
  private var targets = 1
  private var spawnDelay: Double  = 0.5
  private var spawnInterval: Double  = 5.0

  def x: Double = _x
  def y: Double = _y
  def width: Double = 1
  def height: Double = 1
  def die(): Unit = alive = false
  
  def update(delay: Double): Unit = {
    spawnDelay += delay
    targets = level.players.length

    if (spawnDelay >= spawnInterval) {
      if(targets == 1) {
        val enemy = new Enemy(_x, _y, level, health, 1)
        level += enemy
        spawnDelay = 0.0
      } else if(targets == 2) {
        val enemy1 = new Enemy(_x, _y, level, health, 1)
        val enemy2 = new Enemy(_x, _y, level, health, 2)
        level += enemy1
        level += enemy2
        spawnDelay = 0.0
      } else if(targets == 3) {
        val enemy1 = new Enemy(_x, _y, level, health, 1)
        val enemy2 = new Enemy(_x, _y, level, health, 2)
        val enemy3 = new Enemy(_x, _y, level, health, 3)
        level += enemy1
        level += enemy2
        level += enemy3
        spawnDelay = 0.0
      }
      health += 1
    }
    spawnInterval = spawnInterval-(delay/25)
  }

  def postCheck(): Unit = {
    // val touching = level.bullets.find(e => Entity.intersect(this, e))
    // if (touching != None) {
    //   touching.foreach(e => e.die())
    //   health -= 1
    // }
    // if(health == 0) this.die()
  }

  def stillHere(): Boolean = alive

  def buildPassable(): PassableEntity = new PassableEntity(x, y, width, height, EntityType.Generator)
}