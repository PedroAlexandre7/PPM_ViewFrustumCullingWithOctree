package Main

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, TextField}
import Utils.IO_Utils.readFile
import Utils.InitSubScene._
import Utils.OctreeUtils.createOctree
import javafx.stage.{Modality, Stage}

class InsertController {

  @FXML
  private var buttonDone: Button = _

  @FXML
  private var fileTextField: TextField = _


  def onButtonDoneClicked(): Unit = {

    readFile(worldRoot, fileTextField.getText)
    val (oct, part) = createOctree(worldRoot, 10)
    octree = oct
    partitions = part
    val stage: Stage = new Stage()
    stage.setTitle("PPM Project")
    //    secondStage.initOwner(buttonDone.getScene.getWindow)
    val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
    val newStage: Parent = fxmlLoader.load()
    stage.setScene(new Scene(newStage))
    stage.show()

    buttonDone.getScene.getWindow.hide()
  }

}
