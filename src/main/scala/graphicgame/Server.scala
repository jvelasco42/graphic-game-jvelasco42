package graphicgame

import java.net.ServerSocket
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import scala.collection.mutable
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

case class PlayerConnection(player: Player, sock: Socket, in: ObjectInputStream, out: ObjectOutputStream)

object Server extends App {
  val connections = mutable.Buffer[PlayerConnection]()
  val queue = new LinkedBlockingQueue[PlayerConnection]()
  val entities = Seq[Entity]()
  val level = new Level(RandomMaze(3, false, 10, 10, 0.5), entities)
  val generator = new Generator(2, 2, level)
  var playerX = 0
  var playerY = 0
  var playerName = "Player"
  var winner = ""

  level += generator

  val ss = new ServerSocket(8000)
  Future {
    while(true) {
      val sock = ss.accept()
      val ois = new ObjectInputStream(sock.getInputStream())
      val oos = new ObjectOutputStream(sock.getOutputStream())
      if(level.players.length == 0) {
        playerX = 29
        playerY = 29
        playerName = "Player1"
      } else if(level.players.length == 1) {
        playerX = 2
        playerY = 29
        playerName = "Player2"
      } else if(level.players.length == 2) {
        playerX = 29
        playerY = 2
        playerName = "Player3"
      }
      val player = new Player(playerX, playerY, level, playerName)
      level += player
      queue.put(PlayerConnection(player, sock, ois, oos))
    }
  }

  var updateTimer = 0.0
  var updateInterval = 0.1
  var lastTime = -1L
  while(true) {
    while (!queue.isEmpty()) {
      connections += queue.take()
    }
    val time = System.nanoTime()
    if (lastTime >= 0) {
      val delay = (time - lastTime)/1e9
      level.updateAll(delay)
      if(level.players.filter(_.stillHere).length == 1) winner = level.players.filter(_.stillHere).head.name
      updateTimer += delay
      // println(s"Delay = $delay")
      for (pc <- connections) {
        while (pc.in.available() > 0) {
          pc.in.readInt() match {
            case InputData.KeyPressed => pc.player.keyPressed(pc.in.readInt())
            case InputData.KeyReleased => pc.player.keyReleased(pc.in.readInt())
            case InputData.MouseClicked => pc.player.mouseClicked(pc.in.readInt(), pc.in.readInt())
            case i => println("Unhandled input" + i)
          }
        }
      
        if (updateTimer > updateInterval) {
          pc.out.writeObject(new UpdateInfo(level.buildPassable, pc.player.x, pc.player.y, pc.player.currentHealth,
           pc.player.currentMoney, pc.player.currentShot, pc.player.currentTarget, pc.player.name, level.players.length, winner))
          pc.out.flush()
        }
      }
      if (updateTimer > updateInterval) updateTimer = 0.0
    }
    lastTime = time
  }
} 