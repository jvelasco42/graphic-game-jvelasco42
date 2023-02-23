package graphicgame

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import scalafx.scene.text.Text

/**
 * This is a 2D renderer that with draw your game elements to a Canvas. You should change the
 * images to fit the style of your game. Also, alter the entities to match what you have in
 * your game.
 */
class Renderer2D(gc: GraphicsContext, blockSize: Double) {
  private var lastCenterX = 0.0
  private var lastCenterY = 0.0

  // Put variables for images here
  private val floorImage = Renderer2D.loadImage("/images/floor.png")
  private val wallImage = Renderer2D.loadImage("/images/wall.png")
  private val playerImage = Renderer2D.loadImage("/images/player.png")
  private val enemyImage = Renderer2D.loadImage("/images/enemy.gif")
  private val generatorImage = Renderer2D.loadImage("/images/generator.jpg")
  private val bulletImage = Renderer2D.loadImage("/images/bullet.png")
  private val bulletGeneratorImage = Renderer2D.loadImage("/images/bulletGen.png")
  private val missileImage = Renderer2D.loadImage("/images/missile.jpg")
  private val missileGeneratorImage = Renderer2D.loadImage("/images/missileGen.png")

  /**
   * These two methods are used to figure out where to draw things. They are used by the render.
   */
  def blocksToPixelsX(bx: Double): Double = gc.canvas.getWidth / 2 + (bx - lastCenterX) * blockSize
  def blocksToPixelsY(by: Double): Double = gc.canvas.getHeight / 2 + (by - lastCenterY) * blockSize

  /**
   * These two methods are used to go from coordinates to blocks. You need them if you have mouse interactions.
   */
  def pixelsToBlocksX(px: Double): Double = (px - gc.canvas.getWidth / 2) / blockSize + lastCenterX
  def pixelsToBlocksY(py: Double): Double = (py - gc.canvas.getHeight / 2) / blockSize + lastCenterY

  /**
   * This method is called to render things to the screen.
   */
  def render(level: PassableLevel, cx: Double, cy: Double, health: Int, money: Int, shot: String, 
    target: Int, name: String, numAlive: Int, winner: String): Unit = {
    lastCenterX = cx
    lastCenterY = cy

    val drawWidth = (gc.canvas.getWidth / blockSize).toInt + 1
    val drawHeight = (gc.canvas.getWidth / blockSize).toInt + 1

    // Draw walls and floors
    for(x <- cx.toInt - drawWidth / 2 - 1 to cx.toInt + drawWidth / 2 + 1;
      y <- cy.toInt - drawHeight / 2 - 1 to cy.toInt + drawHeight / 2 + 1) {
      val img = level.maze(x, y) match {
        case Wall => wallImage
        case Floor => floorImage
      }
      gc.drawImage(img, blocksToPixelsX(x), blocksToPixelsY(y), blockSize, blockSize)
    }

    // Draw entities
    for(e <- level.entities) {
      val img = e.style match {
        case EntityType.Player => playerImage
        case EntityType.Enemy => enemyImage
        case EntityType.Generator => generatorImage
        case EntityType.Bullet => bulletImage
        case EntityType.BulletGenerator => bulletGeneratorImage
        case EntityType.Missile => missileImage
        case EntityType.MissileGenerator => missileGeneratorImage
      }
      if(level.maze.wrap) {
        for(rx <- -1 to 1; ry <- -1 to 1)
    	    gc.drawImage(img, blocksToPixelsX(e.x-e.width/2+rx*level.maze.width), blocksToPixelsY(e.y-e.height/2+ry*level.maze.height), e.width*blockSize, e.height*blockSize)
      } else {
    	  gc.drawImage(img, blocksToPixelsX(e.x-e.width/2), blocksToPixelsY(e.y-e.height/2), e.width*blockSize, e.height*blockSize)
      }
    }

    
		gc.fillText("Health: " + health, 300, 930)
    gc.fillText("Money: " + money, 300, 950)
    gc.fillText("Current Shot: \n" + shot, 50, 930)
    gc.fillText("Current Target: \n" + target, 150, 930)
    gc.fillText(name, 430, 950)
    if(numAlive <= 1) gc.fillText("Winner: " + winner, 430, 485)
  }

}

object Renderer2D {
  /**
   * This method assumes that you are putting your images in src/main/resources. This directory is
   * packaged into the JAR file. Eclipse doesn't use the JAR file, so this will go to the file in
   * the directory structure if it can't find the resource in the classpath. The argument should be the
   * path inside of the resources directory.
   */
  def loadImage(path: String): Image = {
    val res = getClass.getResource(path)
    if(res == null) {
      new Image("file:src/main/resources"+path)
    } else {
      new Image(res.toExternalForm())
    }
  }  
}
