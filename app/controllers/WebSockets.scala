package controllers

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.mvc.Controller
import play.api.mvc.WebSocket

object WebSockets extends Controller {

	def party(username: String) = WebSocket.async[JsValue] { request =>

		val in = Iteratee.foreach[JsValue](println).mapDone { _ =>
			println("Disconnected")
		}

		val out = Enumerator[JsValue](JsString("Hello!"), JsString("Hello2!"))

		Future(in, out)

	}

}