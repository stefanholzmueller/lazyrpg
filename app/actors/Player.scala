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
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber

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

		case UpdateStats(lvl, xp) => {
			transmitStats(lvl, xp)
		}

	}

	private def transmitLogMessage(kind: String, text: String): Unit = {
		val data = Map("kind" -> kind, "text" -> text)
		val seq = data.mapValues(JsString(_)).toSeq
		transmitJson("log", seq)
	}

	private def transmitStats(lvl: Int, xp: Int): Unit = {
		val data = Map("lvl" -> lvl, "xp" -> xp)
		val seq = data.mapValues(JsNumber(_)).toSeq
		transmitJson("stats", seq)
	}

	private def transmitJson(category: String, seq: Seq[(String, JsValue)]): Unit = {
		val msg = JsObject(Seq(category -> JsObject(seq)))
		chatChannel.push(msg)
	}

}

case class SendLogEntry(kind: String, text: String)
case class UpdateStats(lvl: Int, xp: Int)
