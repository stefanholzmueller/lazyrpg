package actors

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Grinding(player: ActorRef) extends Actor {

	val random = new Random()

	/**
	 * uniformly distributed integer between min (inclusive) and max (inclusive)
	 */
	def randomRange(min: Int, max: Int) = random.nextInt(max - min + 1) + min

	def grindDelay() = FiniteDuration(randomRange(2, 4), TimeUnit.SECONDS)

	def receive = {

		case StartGrinding() => {
			context.system.scheduler.scheduleOnce(grindDelay(), self, KilledSomething())
		}

		case KilledSomething() => {
			player ! GainXp(5)
			self ! StartGrinding() // endless
		}

	}

}

case class StartGrinding()
case class KilledSomething()
