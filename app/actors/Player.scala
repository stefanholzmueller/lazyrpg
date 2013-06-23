package actors

import scala.math.BigDecimal.int2bigDecimal

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.event.LoggingReceive
import controllers.ConnectionRequest
import controllers.ConnectionResponse
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue

class Player(username: String) extends Actor with ActorLogging {

	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	def receive = LoggingReceive {

		case ConnectionRequest() => {
			sender ! ConnectionResponse(chatEnumerator)
		}

		case StartPlaying() => {
			transmitLogMessage("event", "Your adventure starts ...")

			val character = context.system.actorOf(Props(new Character(self)))
			character ! StartLeveling()
		}

		case SendLogEntry(kind, text) => {
			transmitLogMessage(kind, text)
		}

		case UpdateStats(lvl, xp, xpNext) => {
			transmitStats(lvl, xp, xpNext)
		}

	}

	private def transmitLogMessage(kind: String, text: String): Unit = {
		val data = Map("kind" -> kind, "text" -> text)
		transmitJson("log", data.mapValues(JsString(_)))
	}

	private def transmitStats(lvl: Int, xp: Int, xpNext: Int): Unit = {
		val data = Map("lvl" -> lvl, "xp" -> xp, "xpNext" -> xpNext)
		transmitJson("sheet", data.mapValues(JsNumber(_)))
	}

	private def transmitJson(category: String, map: Map[String, JsValue]): Unit = {
		val msg = JsObject(Seq(category -> JsObject(map.toSeq)))
		chatChannel.push(msg)
	}

}

case class StartPlaying()
case class SendLogEntry(kind: String, text: String)
case class UpdateStats(lvl: Int, xp: Int, xpNext: Int)
