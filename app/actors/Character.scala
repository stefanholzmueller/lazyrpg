package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.event.LoggingReceive
import akka.actor.Props

object Character {
	val INITIAL_LEVEL = 1
	val INITIAL_XP = 0
	val XP_PER_LEVEL_BASE = 100
	val XP_PER_LEVEL_SLOPE = 1.5
}

class Character(player: ActorRef) extends Actor with ActorLogging {

	var level: Int = Character.INITIAL_LEVEL
	var experience: Int = Character.INITIAL_XP

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
		val factor = math.pow(Character.XP_PER_LEVEL_SLOPE, level - 1)
		val xpForNextLevel = (Character.XP_PER_LEVEL_BASE * factor).toInt

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
