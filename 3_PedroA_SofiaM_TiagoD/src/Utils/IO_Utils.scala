package Utils

import PureFunctions.giveRGB
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, Shape3D}

import scala.annotation.tailrec
import scala.collection.SortedMap
import scala.io.Source
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object IO_Utils {

  def readFile(worldRoot: Group, filename: String): Unit = {
    //    val filename = getParameters.getRaw.toString.substring(1, getParameters.getRaw.toString.length() - 1)


    for (line <- Source.fromFile(filename).getLines) {
      var obj: Shape3D = null
      val linha = line.split(" ")
      if (linha(0) == "Cylinder") {
        obj = new Cylinder(0.5, 1, 10)
      }
      if (linha(0) == "Box") {
        obj = new Box(1, 1, 1)
      }
      val material = new PhongMaterial()
      val color = giveRGB(linha(1))
      material.setDiffuseColor(Color.rgb(color._1, color._2, color._3))
      obj.setMaterial(material)
      if (linha.size == 8) {
        obj.setTranslateX(linha(2).toDouble)
        obj.setTranslateY(linha(3).toDouble)
        obj.setTranslateZ(linha(4).toDouble)
        obj.setScaleX(linha(5).toDouble)
        obj.setScaleY(linha(6).toDouble)
        obj.setScaleZ(linha(7).toDouble)
      }
      else {
        obj.setTranslateX(0.5) // para os objetos não ficarem fora, interpretamos que a posição (0 0 0)
        obj.setTranslateY(0.5)
        obj.setTranslateZ(0.5)
      }
      worldRoot.getChildren.add(obj)
    }
  }

  def showPrompt(msg: String): Unit = println(msg)


  def optionPrompt(options: SortedMap[Int, String]): Int = {
    println("\n-- Options --")
    options.toList foreach ((option: (Int, String)) => println(option._1 + ") " + option._2))
    getUserInputInt("Choosen option")
  }

  def prompt(msg: String): String = {
    println(msg)
    getUserInput
  }

  @tailrec
  def getUserInputInt(msg: String): Int = {
    print(msg + ": ")
    Try(getUserInput.toInt) match {
      case Success(i) => i
      case Failure(_) => println("Invalid Number!"); getUserInputInt(msg)
    }
  }

  @tailrec
  def getUserInputDouble(msg: String): Double = {
    print(msg + ": ")
    Try(getUserInput.toDouble) match {
      case Success(i) => i
      case Failure(_) => println("Invalid Number!"); getUserInputDouble(msg)
    }
  }

  def getUserInput: String = readLine.trim.toUpperCase


}
