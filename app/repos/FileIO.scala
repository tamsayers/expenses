package repos

import scala.concurrent.Future
import akka.actor.ActorRef
import play.api.libs.concurrent.Akka
import com.teck.fileio.TextFileActor._
import scala.concurrent.ExecutionContext

trait FileIO {
  def save(text: String): Future[Unit]

  def read(): Future[String]
}

class ActorFileIO(textFileActor: ActorRef)(implicit ex: ExecutionContext) extends FileIO {
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._

  implicit val timeout = Timeout(5 seconds)

  def read(): Future[String] = (textFileActor ? GetText).asInstanceOf[Future[FileText]].map(_.text)

  def save(text: String): Future[Unit] = (textFileActor ? Save(text)).asInstanceOf[Future[Unit]]
}
