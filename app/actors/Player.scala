package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import controllers.StartPlaying
import controllers.StartedPlaying
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue

class Player(username: String) extends Actor with ActorLogging {

	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	def receive = {

		case StartPlaying() => {
			sender ! StartedPlaying(chatEnumerator)
			sender ! BeginAdventure()

			val grinding = context.system.actorOf(Props(new Grinding(self)))
			grinding ! StartGrinding()
		}

		case KilledSomething() => {
			val msg = JsObject(
				Seq(
					"message" -> JsString("You killed a critter. Good job!")))
			chatChannel.push(msg)
		}

		case GainXp(xp) => {
			// TODO
		}
	}

}

case class BeginAdventure()
case class GainXp(xp: Int)
