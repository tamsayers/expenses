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
import akka.testkit.ImplicitSender

class TextFileActorSpec extends TestKit(ActorSystem("FileActorSpec"))
    with WordSpecLike
    with MustMatchers
    with BeforeAndAfterAll
    with ImplicitSender {

  val timeout = 500 millis

  trait uninititialisedFileActor {
    val fileIo = TestProbe()
    val fileIoMaker = (_: ActorRefFactory) => fileIo.ref
    val fileActor = TestActorRef(new TextFileActor(fileIoMaker))
    val savedText = "saved content"
  }

  trait inititialisedFileActor extends uninititialisedFileActor {
    fileActor ! Content(text = savedText)
  }

  "creating a text file actor" should {
    "send a read message to the fileIo actor" in new uninititialisedFileActor {
      fileIo.expectMsg(timeout, "initial read request not made", Read)
    }
  }

  "receiving a persisted message" should {
    "set the persisted text to the actor" in new uninititialisedFileActor {
      fileActor ! Content(text = savedText)

      fileActor.underlyingActor.savedText mustBe savedText
    }
  }

  "receiving a save text call" should {
    val saveMessage = Save(text = "text to save")
    "send the persist message to the fileIo actor" in new inititialisedFileActor {
      fileActor ! saveMessage

      fileIo.expectMsgAllOf(timeout, Read, Write(text = saveMessage.text))
      expectMsg(500 millis, "no response received", "ok")
    }
    "update the file content with the new text" in new inititialisedFileActor {
      fileActor ! saveMessage

      fileActor.underlyingActor.savedText mustBe savedText + saveMessage.text
      expectMsg(500 millis, "no response received", "ok")
    }
  }

  "receiving a read call" should {
    "return the current text to sender" in new inititialisedFileActor {
      fileActor ! GetText

      expectMsg(500 millis, "file content not received", FileText(text = savedText))
    }
  }

  override def afterAll() {
    shutdown()
  }
}
