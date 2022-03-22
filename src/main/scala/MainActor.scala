import UserActor.JsonSerializable
import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.scaladsl.Behaviors

object MainActor {
  def apply(scalaChatWindow: ChatControllerImpl): Behavior[JsonSerializable] = start(scalaChatWindow)

  private def start(scalaChatWindow: ChatControllerImpl):Behavior[JsonSerializable] = Behaviors.setup({ context =>
    val actor = context.spawn(UserActor(scalaChatWindow), "user")
    val topic = context.spawn(Topic[JsonSerializable]("topic"), "topic")

    topic ! Topic.Subscribe(actor)

    Behaviors.receiveMessage {
      msg: JsonSerializable =>
        topic ! Topic.Publish(msg)
        Behaviors.same
    }
  })
}