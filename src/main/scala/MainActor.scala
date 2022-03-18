import User.JsonSerializable
import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.scaladsl.Behaviors

object MainActor {
  def apply(scalaChatWindow: ScalaChatWindow): Behavior[JsonSerializable] = start(scalaChatWindow)

  private def start(scalaChatWindow: ScalaChatWindow):Behavior[JsonSerializable] = Behaviors.setup({ context =>
    val actor = context.spawn(User(scalaChatWindow), "user")
    val topic = context.spawn(Topic[JsonSerializable]("topic"), "topic")

    topic ! Topic.Subscribe(actor)

    Behaviors.receiveMessage {
      msg: JsonSerializable =>
        topic ! Topic.Publish(msg)
        Behaviors.same
    }
  })

}