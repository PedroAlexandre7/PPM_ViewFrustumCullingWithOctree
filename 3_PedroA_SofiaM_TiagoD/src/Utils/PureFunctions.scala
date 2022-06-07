package Utils

import Main.Octree
import Utils.IO_Utils.{getUserInputDouble, optionPrompt}
import Utils.OctreeUtils.{Placement, mapColourEffect, scaleObjs, scaleOctree}
import javafx.scene.Node
import javafx.scene.paint.Color

import scala.annotation.tailrec
import scala.collection.SortedMap

object PureFunctions {


  val options: SortedMap[Int, String] = SortedMap[Int, String](
    elems = 1 -> "Set color to Sepia",
    2 -> "Remove green",
    3 -> "Scale",
    0 -> "Show Objects"
  )

  def doNothing(octree: Octree[Placement]): Octree[Placement] = octree

  @tailrec
  def menu(oct1: Octree[Placement], partitions: List[Node]): Unit = {

    val opt: Int = optionPrompt(options)
    if (opt == 0) {}
    else if (opt == 1)
      menu(mapColourEffect(getSepia)(oct1), partitions)
    else if (opt == 2)
      menu(mapColourEffect(removeGreen)(oct1), partitions)
    else if (opt == 3) {
      val factor = getUserInputDouble("Factor")
      menu(scaleOctree(factor)(oct1), scaleObjs(factor, partitions))
    } else {
      println("Invalid option")
      menu(oct1, partitions)
    }
  }

  def giveRGB(str: String): (Int, Int, Int) = {
    @tailrec
    def loop(str: List[Char], r: Int, g: Int, b: Int, count: Int): (Int, Int, Int) = str.head match {
      case ')' => (r, g, b)
      case ',' => loop(str.tail, r, g, b, count + 1)
      case '(' => loop(str.tail, r, g, b, count)
      case _ =>
        if (count == 0)
          loop(str.tail, r * 10 + str.head - 48, g, b, count)
        else if (count == 1)
          loop(str.tail, r, g * 10 + str.head - 48, b, count)
        else
          loop(str.tail, r, g, b * 10 + str.head - 48, count)
    }

    loop(str.toList, 0, 0, 0, 0)
  }

  def removeGreen(c: Color): Color = {
    Color.rgb((c.getRed * 255).toInt, 0, (c.getBlue * 255).toInt)
  }

  def getSepia(c: Color): Color = {
    val r = c.getRed * 255
    val g = c.getGreen * 255
    val b = c.getBlue * 255
    //    println("Cor antiga = R=" + r + " G=" + g + " B=" + b)
    val newRed = (0.4 * r + 0.77 * g + 0.2 * b).round.toInt
    val newGreen = (0.35 * r + 0.69 * g + 0.17 * b).round.toInt
    val newBlue = (0.27 * r + 0.53 * g + 0.13 * b).round.toInt
    //    println("Cor nova = R=" + newRed + " G=" + newGreen + " B=" + newBlue)
    Color.rgb(255 min newRed, 255 min newGreen, 255 min newBlue)
  }


}
