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

class PlayerSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with WordSpec with MustMatchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("Player"))

	override def afterAll {
		system.shutdown()
	}

	"A Player actor" must {

		"start playing" in {
			val player = system.actorOf(Props(new Player("player1")))

			player ! StartPlaying()

			expectMsgClass(classOf[StartedPlaying])
			expectMsg(BeginAdventure())
		}

	}
}