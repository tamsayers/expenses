package com.teck.fileio

import akka.actor.Actor
import java.nio.file.Path
import akka.actor.ActorRefFactory
import akka.actor.ActorRef

object TextFileActor {
  final case class Save(text: String)
  final case class FileText(text: String)
  final case object GetText
}

class TextFileActor(fileIoMaker: ActorRefFactory => ActorRef) extends Actor {
  import TextFileActor._
  import FileIoActor._

  val fileIo = fileIoMaker(context)
  var savedText: String = ""

  fileIo ! Read

  def initialised: Receive = {
    case Save(text) => {
      fileIo ! Write(text)
      savedText = text
      // temporary work around
      sender ! "ok"
    }
    case GetText => sender ! FileText(text = savedText)
    case Persisted =>
  }

  def receive = {
    case Content(content) => {
      savedText = content
      context.become(initialised)
    }
  }
}
