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

class Player(username: String) extends Actor {

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
	}

}

case class BeginAdventure()
case class GainXp(xp: Int)
