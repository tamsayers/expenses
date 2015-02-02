package com.teck.fileio

import java.nio.file.Files
import java.nio.file.Path
import org.scalatest.Finders
import org.scalatest.MustMatchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.actor.ActorRefFactory
import akka.testkit.TestProbe
import org.scalatest.BeforeAndAfterAll
import com.teck.fileio.TextFileActor._
import com.teck.fileio.FileIoActor._
import scala.concurrent.duration._

class TextFileActorSpec extends TestKit(ActorSystem("FileActorSpec")) with WordSpecLike with MustMatchers with BeforeAndAfterAll {

  val timeout = 500 millis

  trait testFileActor {
    val fileIo = TestProbe()
    val fileIoMaker = (_: ActorRefFactory) => fileIo.ref
    val fileActor = TestActorRef(new TextFileActor(fileIoMaker))
  }

  override def afterAll() {
    shutdown()
  }

  "creating a text file actor" should {
    "send a read message to the fileIo actor" in new testFileActor {
      fileIo.expectMsg(timeout, "initial read request not made", Read)
    }
  }

  "receiving a save text call" should {
    val saveMessage = Save(text = "text to save")
    "send the persist message to the fileIo actor" in new testFileActor {
      fileActor ! saveMessage

      fileIo.expectMsgAllOf(timeout, Read, Write(text = saveMessage.text))
    }
    "update the file content with the new text" in new testFileActor {
      fileActor ! saveMessage

      fileActor.underlyingActor.fileContent mustBe saveMessage.text
    }
  }

  "receiving a file contents message" should {
    "set the file content to the actor" in new testFileActor {
      val fileContent = "content"

      fileActor ! Persisted(text = fileContent)

      fileActor.underlyingActor.fileContent mustBe fileContent
    }
  }
}
