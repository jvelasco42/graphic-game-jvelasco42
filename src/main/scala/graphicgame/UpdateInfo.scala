package graphicgame

case class UpdateInfo(level: PassableLevel, px: Double, py: Double, health: Int, money: Int, shot: String, 
    target: Int, name: String, numAlive: Int, winner: String) {
  
}