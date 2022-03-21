import User.{ExitUser, JsonSerializable, PrivateMsg, PublicMsg, SetUsername, WelcomeUser}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object User {
  trait JsonSerializable

  case class PublicMsg(nickname: String, text: String) extends JsonSerializable

  case class PrivateMsg(nickname: String, to: String, message: String) extends JsonSerializable

  case class SetUsername(nickname: String) extends JsonSerializable

  case class WelcomeUser() extends JsonSerializable

  case class ExitUser(nickname: String) extends JsonSerializable

  protected def createActor(scalaMainController: ScalaChatController): Behavior[JsonSerializable] = {
    Behaviors.setup(context => new User(context, scalaMainController))
  }

  def apply(scalaMainController: ScalaChatController): Behavior[JsonSerializable] = createActor(scalaMainController)

  }


class User(context: ActorContext[JsonSerializable], scalaChatWindow: ScalaChatController) extends AbstractBehavior[JsonSerializable](context) {

  val nickname: String = scalaChatWindow.login
  override def onMessage(msg: JsonSerializable): Behavior[JsonSerializable] = {
    msg match {
      case WelcomeUser() =>
        scalaChatWindow.system ! SetUsername(nickname)
        this

      case SetUsername(nickname) =>
        if (!scalaChatWindow.currentOnline.getText.contains(nickname))
          scalaChatWindow.currentOnline.appendText(s"\n<$nickname>")
        this

      case PublicMsg(nickname, message) =>
        scalaChatWindow.msgField.appendText(s"<$nickname>: $message\n")
        this

      case PrivateMsg(nickname, destination, message) =>
        if (nickname.equals(scalaChatWindow.login) | destination.equals(scalaChatWindow.login)) {
          if (destination.isEmpty) {
            scalaChatWindow.msgField.appendText("[ERROR] You must specify a recipient!\n")
          } else
            scalaChatWindow.msgField.appendText(s"[$nickname>>>$destination]: $message\n")
        }
        this

      case  ExitUser(nickname) =>
        scalaChatWindow.currentOnline.setText(scalaChatWindow.currentOnline.getText.replace(s"\n<$nickname>", "").trim)
        this

      case _ =>
        this
    }
  }
}
