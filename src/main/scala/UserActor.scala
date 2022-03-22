import UserActor._
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object UserActor {
  trait JsonSerializable

  case class PublicMsg(nickname: String, text: String) extends JsonSerializable

  case class PrivateMsg(nickname: String, to: String, message: String) extends JsonSerializable

  case class SetUsername(nickname: String) extends JsonSerializable

  case class ExitUser(nickname: String) extends JsonSerializable

  case object WelcomeUser extends JsonSerializable

  protected def createActor(controller: ChatControllerImpl): Behavior[JsonSerializable] = {
    Behaviors.setup(context => new UserActor(context, controller))
  }

  def apply(controller: ChatControllerImpl): Behavior[JsonSerializable] = createActor(controller)

  }


class UserActor(context: ActorContext[JsonSerializable], chatWindow: ChatControllerImpl) extends AbstractBehavior[JsonSerializable](context) {

  override def onMessage(msg: JsonSerializable): Behavior[JsonSerializable] = {
    val currentLogin = chatWindow.login
    msg match {
      case WelcomeUser =>
        chatWindow.system ! SetUsername(currentLogin)
        this

      case SetUsername(nickname) =>
        if (!chatWindow.currentOnline.getText.contains(nickname)) {
          chatWindow.currentOnline.appendText(s"\n<$nickname>")
        }
        this

      case PublicMsg(nickname, message) =>
        chatWindow.msgField.appendText(s"<$nickname>: $message\n")
        this

      case PrivateMsg(nickname, destination, message) =>
        if (nickname.equals(currentLogin) || destination.equals(currentLogin)) {
          if (destination.isEmpty) {
            chatWindow.msgField.appendText("[ERROR] You must specify a recipient!\n")
          } else {
            chatWindow.msgField.appendText(s"[$nickname>>>$destination]: $message\n")
          }
        }
        this

      case ExitUser(nickname) =>
        chatWindow.currentOnline.setText(chatWindow.currentOnline.getText.replace(s"\n<$nickname>", "").trim)
        this

      case _ =>
        this
    }
  }
}
