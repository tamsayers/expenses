import scala.concurrent.ExecutionContext
import java.io.File
import scala.io.Source
import java.io.PrintWriter
import akka.actor.ActorRefFactory
import akka.actor.ActorRef

package object repos {
  import com.softwaremill.macwire._

  trait ReposModule {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    val textFileActor: ActorRef
    val fileIo: FileIO = wire[ActorFileIO]
    val expensesRepository = wire[JsonExpensesRepository]
  }
}
