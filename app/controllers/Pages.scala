package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent

object Pages extends Controller {

	def index() = Action {
		sendFile("index.html")
	}

	def player(username: String) = Action {
		sendFile("play.html")
	}

	def constants = Action {
		Ok(views.html.constants())
	}

	private def sendFile(path: String) = {
		val stream = getClass().getResourceAsStream("/public/ui/" + path)
		val string = io.Source.fromInputStream(stream).mkString
		Ok(string) as HTML
	}

}

