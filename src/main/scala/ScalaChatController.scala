import User.{ExitUser, JsonSerializable, PrivateMsg, PublicMsg, SetUsername, WelcomeUser}
import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.cluster.typed.Cluster
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.event.ActionEvent




class ScalaChatController extends JavaChatController {

  var login: String = _
  var system: ActorSystem[JsonSerializable] = _

  def startSystem(port: String, address: String) :Unit = {
    val config = ConfigFactory.parseString(s"""
            akka.remote.artery.canonical.port=$port
            akka.cluster.seed-nodes = ["akka://system@$address:2551"]
            """).withFallback(ConfigFactory.load())
    this.system = ActorSystem(MainActor(this), "system", config)
    val cluster = Cluster(this.system)
    setOnline()
  }

  protected def setOnline(): Unit = {
    val nickname = login
    usernameDisplay.setText(s"<$nickname>")
    Thread.sleep(3000) // Ожидание пока запустится акторная система
    this.system ! WelcomeUser()
  }

  override def actionSendButton(event: ActionEvent): Unit = {
    val nickname = login
    val message = sendInput.getText.trim
    if (message.nonEmpty) {
      message match {

        case input: String if message.startsWith("/whisper") =>
          val part = input.split("@", 3)
          if (part.length <= 2) {
            system ! PublicMsg(nickname, input)
          } else {
            val destination = part(1)
            val message = part(2)
            system ! PrivateMsg(nickname, destination, message)
          }
        case _ => system ! PublicMsg(nickname, message)
      }
      sendInput.clear()
    }
  }

  override def actionExitButton(event: ActionEvent): Unit = {
    val nickname = login
    this.system ! ExitUser(nickname)
    this.system.terminate()
    Platform.exit()
    System.exit(0)
  }

}