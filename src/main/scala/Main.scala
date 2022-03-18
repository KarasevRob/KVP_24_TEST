import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.paint.Color
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, StageStyle}


class Main extends Application {
  def start(stage: Stage): Unit = {
    val root: Parent = FXMLLoader.load(getClass.getResource("/FXML/LoginScreen.fxml"))
    stage.setScene(new Scene(root))
    stage.setTitle("KVP24-Chat")
    stage.setResizable(false)
    stage.show()

  }
}