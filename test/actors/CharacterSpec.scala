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
import play.api.test.FakeApplication
import play.api.test.Helpers.running

class CharacterSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with WordSpec with MustMatchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("Character"))

	override def afterAll {
		system.shutdown()
	}

	"A Character actor" must {

		"gain experience" in {
			running(FakeApplication()) {
				val character = system.actorOf(Props(new Character(testActor)))

				character ! GainXp(5)

				expectMsgClass(classOf[SendLogEntry])
				expectMsg(UpdateStats(1, 5, 100))

				character ! GainXp(10)

				expectMsgClass(classOf[SendLogEntry])
				expectMsg(UpdateStats(1, 15, 100))
			}
		}

		"level up" in {
			running(FakeApplication()) {
				val character = system.actorOf(Props(new Character(testActor)))

				character ! GainXp(123)

				expectMsgClass(classOf[SendLogEntry])
				expectMsg(SendLogEntry("lvlup", "You have reached level 2!"))
				expectMsg(UpdateStats(2, 23, 150))
			}
		}

		"level up recursively" in {
			running(FakeApplication()) {
				val character = system.actorOf(Props(new Character(testActor)))

				character ! GainXp(500)

				expectMsgClass(classOf[SendLogEntry])
				expectMsg(SendLogEntry("lvlup", "You have reached level 2!"))
				expectMsg(SendLogEntry("lvlup", "You have reached level 3!"))
				expectMsg(SendLogEntry("lvlup", "You have reached level 4!"))
				expectMsg(UpdateStats(4, 25, 337))
				expectNoMsg()
			}
		}

	}
}