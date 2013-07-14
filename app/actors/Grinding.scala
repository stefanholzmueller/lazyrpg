package actors

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.event.LoggingReceive
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Grinding(character: ActorRef) extends Actor {

	val random = new Random()
	val delayTimeunit = {
		val option = Play.current.configuration.getString("delay.timeunit")
		TimeUnit.valueOf(option.getOrElse("SECONDS"))
	}

	/**
	 * uniformly distributed integer between min (inclusive) and max (inclusive)
	 */
	def randomRange(min: Int, max: Int): Int = random.nextInt(max - min + 1) + min

	def grindDelay() = FiniteDuration(randomRange(5, 10), delayTimeunit)

	def receive = LoggingReceive {

		case StartGrinding() => {
			context.system.scheduler.scheduleOnce(grindDelay(), self, DoneGrinding())
		}

		case DoneGrinding() => {
			character ! GainXp(randomRange(3, 6))
			self ! StartGrinding() // endless
		}

	}

}

case class StartGrinding()
case class DoneGrinding()
