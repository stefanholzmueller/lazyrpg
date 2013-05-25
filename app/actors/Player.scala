package actors

import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsValue
import akka.actor.Actor
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import controllers.StartPlaying
import controllers.StartedPlaying

class Player extends Actor {

	val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

	var name = ""

	def receive = {

		case StartPlaying(username) => {
			name = username
			sender ! StartedPlaying(chatEnumerator)
		}

		case KilledCritter() => {
			val msg = JsObject(
				Seq(
					"message" -> JsString("You killed a critter. Good job!")))
			chatChannel.push(msg)
		}
	}

}

case class Talk(text: String)
case class KilledCritter()
