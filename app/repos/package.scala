import scala.concurrent.ExecutionContext
import java.io.File
import scala.io.Source
import java.io.PrintWriter
import akka.actor.ActorRefFactory
import akka.actor.ActorRef

package object repos {
  import com.softwaremill.macwire._

  trait ReposModule {
    implicit val ex: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext

    def textFileActor: ActorRef
    lazy val fileIo: FileIO = wire[ActorFileIO]
    lazy val expensesRepository = wire[JsonExpensesRepository]
  }
}
