import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import actors.Player
import actors.SendLogEntry
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import controllers.ConnectionRequest
import controllers.ConnectionResponse
import actors.UpdateStats

class PlayerSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with WordSpec with MustMatchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("Player"))

	override def afterAll {
		system.shutdown()
	}

	"A Player actor" must {

		"establish the connection" in {
			val player = system.actorOf(Props(new Player("player1")))

			player ! ConnectionRequest()

			expectMsgClass(classOf[ConnectionResponse])
			expectNoMsg()
		}

	}
}