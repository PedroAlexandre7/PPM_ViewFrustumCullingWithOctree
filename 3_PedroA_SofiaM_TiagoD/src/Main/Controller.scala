package Main

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, SubScene}
import javafx.scene.control._
import Utils.InitSubScene._
import Utils.OctreeUtils._
import Utils.PureFunctions._
import Utils.InitSubScene
import javafx.scene.Node

class Controller {

  val originalOctree: Octree[Placement] = octree
  val originalPartitions: List[Node] = partitions

  @FXML
  private var subScene1: SubScene = _

  @FXML
  private var removeGreenButton: Button = _

  @FXML
  private var x0_5Button: Button = _

  @FXML
  private var x2Button: Button = _

  @FXML
  private var setSepiaButton: Button = _

  //  @FXML
  //  private var scaleSlider: Slider = _


  def onRemoveGreenButtonClicked(): Unit = {
    octree = mapColourEffect(removeGreen)(octree)
  }

  def onSetSepiaButtonClicked(): Unit = {
    octree = mapColourEffect(getSepia)(octree)
  }


  def onX0_5ButtonClicked(): Unit = {
    octree = scaleOctree(0.5)(octree)
    partitions = scaleObjs(0.5, partitions)
  }

  def onX2ButtonClicked(): Unit = {
    octree = scaleOctree(2)(octree)
    partitions = scaleObjs(2, partitions)
  }



  //  def scaleSliderDrag(): Unit = {
  //    val fact = scaleSlider.getValue / 50
  //    println("slider" + fact)
  //    octree = scaleOctree(fact)(originalOctree)
  //    partitions = scaleObjs(fact, originalPartitions)
  //  }

  //method automatically invoked after the @FXML fields have been injected
  @FXML
  def initialize(): Unit = {
    val fjkhsd = subScene1.widthProperty
    InitSubScene.subScene.widthProperty.bind(fjkhsd)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
  }

}
