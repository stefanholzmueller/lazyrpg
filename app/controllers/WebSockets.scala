package controllers

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

import actors.Player
import actors.StartPlaying
import actors.StopPlaying
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue
import play.api.mvc.Controller
import play.api.mvc.WebSocket

object WebSockets extends Controller {

	implicit val timeout = Timeout(1 second) // needed for `?`

	def player(username: String): WebSocket[JsValue] = {
		val player = Akka.system.actorOf(Props(new Player(username)))

		WebSocket.async[JsValue] { request =>

			(player ? ConnectionRequest()).map {

				case ConnectionResponse(enumerator) => {
					println("Connected: " + username)

					val iteratee = Iteratee.foreach[JsValue] { _ =>
						player ! StartPlaying()
					}.mapDone { _ =>
						player ! StopPlaying()
						println("Disconnected: " + username)
					}

					(iteratee, enumerator)
				}
			}
		}
	}

}

case class ConnectionRequest()
case class ConnectionResponse(enumerator: Enumerator[JsValue])
