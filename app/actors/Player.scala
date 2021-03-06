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
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper

class Player(username: String) extends Actor with ActorLogging {

	val character = context.system.actorOf(Props(new Character(self)))
	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	def receive = LoggingReceive {

		case ConnectionRequest() => {
			sender ! ConnectionResponse(chatEnumerator)
		}

		case StartPlaying() => {
			transmitLogMessage("event", "Your adventure starts ...")

			character ! StartLeveling()
		}

		case StopPlaying() => {
			context.stop(self)
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
		val msg = Json.obj(category -> JsObject(map.toSeq))
		chatChannel.push(msg)
	}

	override def postStop() = {
		context.stop(character)
	}

}

case class StartPlaying()
case class StopPlaying()
case class SendLogEntry(kind: String, text: String)
case class UpdateStats(lvl: Int, xp: Int, xpNext: Int)
