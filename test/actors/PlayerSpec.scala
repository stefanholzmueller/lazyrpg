import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

import actors.Player
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import controllers.ConnectionRequest
import controllers.ConnectionResponse
import play.api.test.FakeApplication
import play.api.test.Helpers.running

class PlayerSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with WordSpec with MustMatchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("Player"))

	override def afterAll {
		system.shutdown()
	}

	"A Player actor" must {

		"establish the connection" in {
			running(FakeApplication()) {
				val player = system.actorOf(Props(new Player("player1")))

				player ! ConnectionRequest()

				expectMsgClass(classOf[ConnectionResponse])
				expectNoMsg()
			}
		}

	}
}