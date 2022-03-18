import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage


class ScalaLoginWindow extends JavaLoginWindow {
  override def actionLogButton(event: ActionEvent): Unit = {
    if (loginInput.getText.trim.nonEmpty & portInput.getText.trim.nonEmpty) {
      logButton.getScene.getWindow.hide()
      val controller = getRootController[ScalaChatWindow]("FXML/ChatScreen.fxml")
      val stage = new Stage()
      controller._2.login = loginInput.getText.trim
      controller._2.startSystem(portInput.getText.trim, if (ipInput.getText.trim.isEmpty) "localhost" else ipInput.getText.trim)
      stage.setScene(new Scene(controller._1))
      stage.setTitle("KVP24-Chat")
      stage.setResizable(false)
      stage.showAndWait()
    } else {
      wrongLogin.setText("You must enter your nickname & port!")
    }
  }

  def getRootController[T](path: String): (Parent, T) = {
    val url = getClass.getClassLoader.getResource(path)
    val loader = new FXMLLoader(url)
    val root = loader.load[Parent]()
    val controller = loader.getController[T]
    (root, controller)
  }
}


