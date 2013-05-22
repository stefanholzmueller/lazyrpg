package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.JsValue

object Application extends Controller {

	def party = WebSocket.async[JsValue] { request =>
		???
	}

}