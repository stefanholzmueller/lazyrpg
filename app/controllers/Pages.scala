package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object Pages extends Controller {

	def index() = Action { implicit request =>
		sendFile("/public/ui/index.html")
	}

	def player(username: String) = Action { implicit request =>
		sendFile("/public/ui/play.html")
	}

	private def sendFile(path: String) = {
		val fileUrl: java.net.URL = getClass().getResource(path)
		val file = new java.io.File(fileUrl.toURI())
		Ok.sendFile(file, inline = true)
	}

}

