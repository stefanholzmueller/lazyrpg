package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.event.LoggingReceive
import akka.actor.Props

class Character(player: ActorRef) extends Actor with ActorLogging {

	val LEVEL_BASE_XP: Int = 100

	var level: Int = 1
	var experience: Int = 0

	def receive = LoggingReceive {

		case StartLeveling() => {
			val grinding = context.system.actorOf(Props(new Grinding(self)))
			grinding ! StartGrinding()
		}

		case GainXp(xp) => {
			experience += xp

			player ! SendLogEntry("kill", s"You slay a random monster for $xp XP")

			handleLevelUp()

			player ! UpdateStats(level, experience)
		}

	}

	private def handleLevelUp(): Unit = {
		val xpForNextLevel = (LEVEL_BASE_XP * math.pow(1.5, level - 1)).toInt

		if (experience >= xpForNextLevel) {
			experience -= xpForNextLevel
			level += 1

			player ! SendLogEntry("lvlup", s"You have reached level $level!")

			handleLevelUp()
		}
	}

}

case class StartLeveling()
case class GainXp(xp: Int)
