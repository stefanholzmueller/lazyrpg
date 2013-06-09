import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import actors.Character
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import actors.GainXp
import actors.UpdateStats
import actors.SendLogEntry

class CharacterSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with WordSpec with MustMatchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("Character"))

	override def afterAll {
		system.shutdown()
	}

	"A Character actor" must {

		"gain experience" in {
			val character = system.actorOf(Props(new Character(testActor)))

			character ! GainXp(5)

			expectMsgClass(classOf[SendLogEntry])
			//			expectMsg(UpdateStats(5))

			character ! GainXp(10)

			expectMsgClass(classOf[SendLogEntry])
			//			expectMsg(UpdateStats(15))
		}

		"level up" in {
			val character = system.actorOf(Props(new Character(testActor)))

			character ! GainXp(123)

			expectMsgClass(classOf[SendLogEntry])
			expectMsg(SendLogEntry("lvlup", "You have reached level 2!"))
		}

		"level up recursively" in {
			val character = system.actorOf(Props(new Character(testActor)))

			character ! GainXp(500)

			expectMsgClass(classOf[SendLogEntry])
			expectMsg(SendLogEntry("lvlup", "You have reached level 2!"))
			expectMsg(SendLogEntry("lvlup", "You have reached level 3!"))
			expectMsg(SendLogEntry("lvlup", "You have reached level 4!"))
			expectNoMsg()
		}

	}
}