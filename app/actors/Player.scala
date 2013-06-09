package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.event.LoggingReceive
import controllers.ConnectionRequest
import controllers.ConnectionResponse
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue

class Player(username: String) extends Actor with ActorLogging {

	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	def receive = LoggingReceive {

		case ConnectionRequest() => {
			sender ! ConnectionResponse(chatEnumerator)
			transmitLogMessage("event", "Your adventure starts ...")

			val character = context.system.actorOf(Props(new Character(self)))
			character ! StartLeveling()
		}

		case SendLogEntry(kind, text) => {
			transmitLogMessage(kind, text)
		}

	}

	private def transmitLogMessage(kind: String, text: String): Unit = {
		val data = Map("kind" -> kind, "text" -> text)
		transmitMessage("log", data)
	}

	private def transmitMessage(category: String, data: Map[String, String]) = {
		val seq = data.mapValues(JsString(_)).toSeq
		val msg = JsObject(Seq(category -> JsObject(seq)))
		chatChannel.push(msg)
	}

}

case class SendLogEntry(kind: String, text: String)
