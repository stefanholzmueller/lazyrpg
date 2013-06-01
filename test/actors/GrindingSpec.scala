import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

import actors.GainXp
import actors.Grinding
import actors.KilledSomething
import actors.StartGrinding
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit

class GrindingSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with WordSpec with MustMatchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("Grinding"))

	override def afterAll {
		system.shutdown()
	}

	"A Grinding actor" must {

		"start grinding" in {
			val grinding = system.actorOf(Props(new Grinding(testActor)))

			grinding ! StartGrinding()

			expectMsgClass(FiniteDuration(11, TimeUnit.SECONDS), classOf[KilledSomething])
			expectMsgClass(FiniteDuration(1, TimeUnit.SECONDS), classOf[GainXp])
			expectMsgClass(FiniteDuration(11, TimeUnit.SECONDS), classOf[KilledSomething])
			expectMsgClass(FiniteDuration(1, TimeUnit.SECONDS), classOf[GainXp])
		}

	}
}