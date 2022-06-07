package Main

import Utils.PureFunctions._
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene._
import javafx.stage.Stage
import Utils.InitSubScene._


class Main extends Application {

  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size

  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node]) //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))

  /*
      Additional information about JavaFX basic concepts (e.g. Stage, Scene) will be provided in week7
     */
  override def start(stage: Stage): Unit = {

    stage.setTitle("PPM Project")

    //para usar o menu TextBased, ir ao InitSubScene e definir a val useMenuTextBased como true
    //para usar o menu UIBased, ir ao InitSubScene e definir a val useMenuTextBased como false
    if (useMenuTextBased) {
      val scene = new Scene(root, 810, 610, true, SceneAntialiasing.BALANCED)
      menu(octree, partitions)
      stage.setScene(scene)
    } else {
      val fxmlLoader = new FXMLLoader(getClass.getResource("FileName.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val scene = new Scene(mainViewRoot)
      stage.setScene(scene)
    }
    stage.show()

  }

  override def init(): Unit = {
    println("init")
  }

  override def stop(): Unit = {
    println("stopped")
  }
}

object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}

