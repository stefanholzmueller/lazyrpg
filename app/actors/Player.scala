package actors

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import akka.actor.Actor
import akka.actor.actorRef2Scala
import controllers.StartPlaying
import controllers.StartedPlaying
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import akka.actor.Props

class Player extends Actor {

	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	var name: Option[String] = None

	def receive = {

		case StartPlaying(username) => {
			name = Some(username)

			sender ! StartedPlaying(chatEnumerator)
			sender ! BeginAdventure()

			val grinding = context.system.actorOf(Props(new Grinding(self)))
			grinding ! StartGrinding()
		}

		case KilledCritter(duration) => {
			val msg = JsObject(
				Seq(
					"message" -> JsString(s"You killed a critter in $duration seconds. Good job!")))
			chatChannel.push(msg)
			val next = duration * 2
			context.system.scheduler.scheduleOnce(next seconds, self, KilledCritter(next))
		}
	}

}

case class KilledCritter(duration: Int)
case class BeginAdventure()
case class GainXp(xp: Int)
