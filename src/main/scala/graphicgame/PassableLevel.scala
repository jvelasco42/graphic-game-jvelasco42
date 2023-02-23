package graphicgame

case class PassableLevel(maze: Maze, passEntities: Seq[PassableEntity]) {
  def entities: Seq[PassableEntity] = passEntities
}