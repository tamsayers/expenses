package com.teck.fileio

import akka.actor.Actor
import java.nio.file.Path
import java.nio.file.Files._
import com.teck.fileio.TextFileActor.Persisted

class FileIoActor(filePath: Path) extends Actor {
  import FileIoActor._
  import java.nio.file.StandardOpenOption._

  def receive = {
    case Read => {
      val text = new String(readAllBytes(filePath), "UTF-8")
      sender ! Persisted(text = text)
    }
    case Write(text) => write(filePath, text.getBytes("UTF-8"), WRITE, TRUNCATE_EXISTING, CREATE)
  }
}

object FileIoActor {
  case object Read
  case class Write(text: String)
}
