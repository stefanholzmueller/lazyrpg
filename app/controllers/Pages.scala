package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent

object Pages extends Controller {

	def index() = Action { implicit request =>
		sendFile("index.html", request)
	}

	def player(username: String) = Action { implicit request =>
		sendFile("play.html", request)
	}

	private def sendFile(path: String, request: Request[AnyContent]) = {
		Assets.at("/public/ui/", path)(request)
	}

}

