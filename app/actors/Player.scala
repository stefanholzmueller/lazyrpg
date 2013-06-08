package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.event.LoggingReceive
import controllers.StartPlaying
import controllers.StartedPlaying
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue

class Player(username: String) extends Actor with ActorLogging {

	val LOG_EVENT = "event"
	val LOG_KILL = "kill"

	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	def receive = LoggingReceive {

		case StartPlaying() => {
			sender ! StartedPlaying(chatEnumerator)

			val grinding = context.system.actorOf(Props(new Grinding(self)))
			grinding ! StartGrinding()

			sendLogMessage(LOG_EVENT, "Your adventure starts ...")
		}

		case KilledSomething() => {
			sendLogMessage(LOG_KILL, "You kill a critter. Good job!")
		}

		case GainXp(xp) => {
			// TODO
		}
	}

	private def sendLogMessage(kind: String, text: String): Unit = {
		val msg = JsObject(
			Seq("log" -> JsObject(Seq(
				"kind" -> JsString(kind),
				"text" -> JsString(text)))))
		chatChannel.push(msg)
	}

}

case class GainXp(xp: Int)
