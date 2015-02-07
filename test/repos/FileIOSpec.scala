package repos

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import akka.actor.ActorRef
import play.api.test.FutureAwaits
import play.api.test.DefaultAwaitTimeout
import org.mockito.Mockito._
import com.teck.fileio.TextFileActor
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.TestProbe
import akka.testkit.ImplicitSender
import org.scalatest.MustMatchers
import org.scalatest.BeforeAndAfterAll

class FileIOSpec extends TestKit(ActorSystem("FileIOSpec"))
    with WordSpecLike
    with MustMatchers
    with FutureAwaits
    with DefaultAwaitTimeout
    with BeforeAndAfterAll {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  trait testFileIO {
    val actorProbe = TestProbe()
    val fileIO = new ActorFileIO(actorProbe.ref)
  }

  "save" should {
    "send a save text message to the text file actor" in new testFileIO {
      val result = fileIO.save("text")

      actorProbe.expectMsg(TextFileActor.Save("text"))
      actorProbe.reply(Unit)
      await(result) mustBe Unit
    }
  }

  "read" should {
    "return the read message response" in new testFileIO {
      val result = fileIO.read()

      actorProbe.expectMsg(TextFileActor.GetText)
      actorProbe.reply(TextFileActor.FileText(text = "text"))
      await(result) mustBe "text"
    }
  }

  override def afterAll() {
    shutdown()
  }
}
