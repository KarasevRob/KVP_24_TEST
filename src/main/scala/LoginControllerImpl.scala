import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage


class LoginControllerImpl extends LoginController {

  override def actionLogButton(event: ActionEvent): Unit = {
    if (loginInput.getText.trim.nonEmpty & portInput.getText.trim.nonEmpty) {
      logButton.getScene.getWindow.hide()

      val rootController = getRootController[ChatControllerImpl]("FXML/ChatScreen.fxml")
      val stage = new Stage()
      val host = if (ipInput.getText.trim.isEmpty) "localhost" else ipInput.getText.trim

      rootController.controller.login = loginInput.getText.trim
      rootController.controller.startSystem(portInput.getText.trim, host)

      stage.setScene(new Scene(rootController.parent))
      stage.setTitle("KVP24-Chat")
      stage.setResizable(false)
      stage.showAndWait()
    } else {
      wrongLogin.setText("You must enter your nickname & port!")
    }
  }

  def getRootController[T](path: String): RootController[T] = {
    val url = getClass.getClassLoader.getResource(path)
    val loader = new FXMLLoader(url)
    val root = loader.load[Parent]()
    val controller = loader.getController[T]
    RootController(root, controller)
  }
  case class RootController[T](parent: Parent, controller: T)
}


