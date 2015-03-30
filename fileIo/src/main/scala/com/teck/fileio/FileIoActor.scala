package com.teck.fileio

import akka.actor.Actor
import java.nio.file.Path
import java.nio.file.Files._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRefFactory
import akka.actor.ActorRef

class FileIoActor(filePath: Path) extends Actor {
  import FileIoActor._
  import java.nio.file.StandardOpenOption._

  def receive = {
    case Read => {
      val text = new String(readAllBytes(filePath), "UTF-8")
      sender ! Content(text = text)
    }
    case Write(text) => {
      write(filePath, text.getBytes("UTF-8"), WRITE, TRUNCATE_EXISTING, CREATE)
      sender ! Persisted
    }
  }
}

object FileIoActor {
  case object Read
  case object Persisted
  final case class Content(text: String)
  final case class Write(text: String)

  def fileIoMakerFor(path: Path): ActorRefFactory => ActorRef = { factory =>
    factory.actorOf(Props(classOf[FileIoActor], path))
  }
}
