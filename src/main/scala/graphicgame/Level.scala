package graphicgame

class Level(val maze: Maze, private var _entities: Seq[Entity]) {
  def entities: Seq[Entity] = _entities
  def +=(e: Entity): Unit = _entities +:= e

  def players = entities.collect { case e: Player  => e }
  def enemies = entities.collect { case e: Enemy  => e }
  def generators = entities.collect { case e: Generator => e }
  def bullets = entities.collect { case e: Bullet => e }
  def missiles = entities.collect { case e: Missile => e }
  def bulletGenerators = entities.collect { case e: BulletGenerator => e }
  def missileGenerators = entities.collect { case e: MissileGenerator => e }

  def updateAll(delay: Double): Unit = {
    _entities.foreach(_.update(delay))
    _entities.foreach(_.postCheck)
    _entities = entities.filter(_.stillHere)
  }

  def buildPassable: PassableLevel = {
    val passEntities = for (e <- entities) yield {
      e.buildPassable
    }
    new PassableLevel(maze, passEntities)
  }
}
