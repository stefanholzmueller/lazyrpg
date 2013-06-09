package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.event.LoggingReceive
import akka.actor.Props

class Character(player: ActorRef) extends Actor with ActorLogging {

	def receive = LoggingReceive {

		case StartLeveling() => {
			val grinding = context.system.actorOf(Props(new Grinding(self)))
			grinding ! StartGrinding()
		}

		case GainXp(xp) => {
			player ! SendLogEntry("kill", s"You slay a random monster for $xp XP")
		}

	}

}

case class StartLeveling()
case class GainXp(xp: Int)
