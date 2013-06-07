package controllers

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

import actors.Player
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import play.libs.Akka

object WebSockets extends Controller {

	implicit val timeout = Timeout(1 second) // needed for `?`

	def player(username: String): WebSocket[JsValue] = {
		val playerActor = Akka.system.actorOf(Props(new Player(username)))

		WebSocket.async[JsValue] { request =>

			(playerActor ? StartPlaying()).map {

				case StartedPlaying(enumerator) => {
					val iteratee = Iteratee.foreach[JsValue](println).mapDone { _ =>
						println("Disconnected: " + username)
					}

					(iteratee, enumerator)
				}
			}
		}
	}

}

case class StartPlaying()
case class StartedPlaying(enumerator: Enumerator[JsValue])
