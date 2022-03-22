import UserActor._
import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.event.ActionEvent

class ChatControllerImpl extends ChatController {

  var login: String = _
  var system: ActorSystem[JsonSerializable] = _

  def startSystem(port: String, address: String) :Unit = {
    val config = ConfigFactory.parseString(s"""
            akka.remote.artery.canonical.port=$port
            akka.cluster.seed-nodes = ["akka://system@$address:2551"]
            """).withFallback(ConfigFactory.load())
    system = ActorSystem(MainActor(this), "system", config)
    setOnline()
  }

  protected def setOnline(): Unit = {
    usernameDisplay.setText(s"<$login>")
    Thread.sleep(3000) // Ожидание пока запустится акторная система
    system ! WelcomeUser
  }

  override def actionSendButton(event: ActionEvent): Unit = {
    val message = sendInput.getText.trim
    if (message.nonEmpty) {
      message match {
        case input: String if message.startsWith("/whisper") =>
          val part = input.split("@", 3)
          if (part.length <= 2) {
            system ! PublicMsg(login, input)
          } else {
            val destination = part(1)
            val message = part(2)
            system ! PrivateMsg(login, destination, message)
          }
        case _ => system ! PublicMsg(login, message)
      }
      sendInput.clear()
    }
  }

  override def actionExitButton(event: ActionEvent): Unit = {
    system ! ExitUser(login)
    system.terminate()
    Platform.exit()
    System.exit(0)
  }
}