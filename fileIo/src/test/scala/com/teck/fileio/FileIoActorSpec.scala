package com.teck.fileio

import org.scalatest._
import akka.testkit._
import akka.actor.ActorSystem
import java.nio.file._
import com.teck.fileio.FileIoActor._
import scala.concurrent.duration._

class FileIoActorSpec extends TestKit(ActorSystem("FileActorSpec")) with ImplicitSender with WordSpecLike with MustMatchers with BeforeAndAfterAll {
  val testFilePath = Files.createTempFile("fileIoActor", ".text")
  val fileActor = TestActorRef(new FileIoActor(testFilePath))

  val text = "file text"

  override def beforeAll() {
    import java.nio.file.StandardOpenOption._
    Files.write(testFilePath, text.getBytes("UTF-8"), WRITE, TRUNCATE_EXISTING)
  }

  "read message" should {
    "read the file contents and return to sender" in {
      fileActor ! Read

      expectMsg(1 second, "file contents not returned", Content(text = text))
    }
  }

  "save text" should {
    import scala.collection.JavaConversions._

    "persist the text to the file" in {
      fileActor ! Write(text = "new file contents")

      val expectedContents = text + "new file contents"

      awaitAssert(Files.readAllLines(testFilePath).mkString mustBe expectedContents, 2 seconds, 500 millis )
    }
    "responds when written" in {
      fileActor ! Write(text = "to write")

      expectMsg(1 second, "no written response", Persisted)
    }
  }

  override def afterAll() {
    shutdown()
  }
}
