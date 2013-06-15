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

	def experienceForNextLevel: Int = {
		val factor = math.pow(Character.XP_PER_LEVEL_SLOPE, level - 1)
		(Character.XP_PER_LEVEL_BASE * factor).toInt
	}

	def receive = LoggingReceive {

		case StartLeveling() => {
			val grinding = context.system.actorOf(Props(new Grinding(self)))
			grinding ! StartGrinding()
		}

		case GainXp(xp) => {
			experience += xp

			player ! SendLogEntry("kill", s"You slay a random monster for $xp XP")

			handleLevelUp()

			player ! UpdateStats(level, experience, experienceForNextLevel)
		}

	}

	private def handleLevelUp(): Unit = {
		if (experience >= experienceForNextLevel) {
			experience -= experienceForNextLevel
			level += 1

			player ! SendLogEntry("lvlup", s"You have reached level $level!")

			handleLevelUp()
		}
	}

}

case class StartLeveling()
case class GainXp(xp: Int)
