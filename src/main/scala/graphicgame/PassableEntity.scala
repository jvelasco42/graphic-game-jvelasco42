package graphicgame

case class PassableEntity(x: Double, y: Double, enWidth: Double, enHeight: Double, style: EntityType.Value)  {
  def width: Double = enWidth
  def height: Double = enHeight
}