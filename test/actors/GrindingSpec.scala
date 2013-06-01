import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import actors.BeginAdventure
import actors.Player
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import controllers.StartPlaying
import controllers.StartedPlaying
import actors.StartGrinding
import actors.Grinding
import actors.GainXp
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

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

			expectMsgClass(FiniteDuration(5, TimeUnit.SECONDS), classOf[GainXp])
		}

	}
}