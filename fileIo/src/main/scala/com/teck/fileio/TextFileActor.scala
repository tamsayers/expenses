package com.teck.fileio

import akka.actor.Actor
import java.nio.file.Path
import akka.actor.ActorRefFactory
import akka.actor.ActorRef

object TextFileActor {
  final case class Save(text: String)
  final case class Persisted(text: String)
}

class TextFileActor(fileIoMaker: ActorRefFactory => ActorRef) extends Actor {
  import TextFileActor._
  import FileIoActor._

  val fileIo = fileIoMaker(context)
  var fileContent: String = ""

  fileIo ! Read

  def receive = {
    case Save(text) => {
      fileIo ! Write(text)
      fileContent = text
    }
    case Persisted(content) => fileContent = content
  }
}
