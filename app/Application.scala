import com.softwaremill.macwire.MacwireMacros._
import controllers.ExpensesController
import services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.async.Async.async
import akka.actor.ActorSystem
import akka.actor.ActorRef
import com.teck.fileio.TextFileActor
import akka.actor.Props
import com.teck.fileio.FileIoActor
import java.nio.file.Paths

object Application extends ServicesModule {
  val actorSystem = ActorSystem("expenses")

  val userHome = System.getProperty("user.home")
  val fileIoMaker = FileIoActor.fileIoMakerFor(Paths.get(userHome, "expenses.json"))
  val textFileActor: ActorRef = actorSystem.actorOf(Props(classOf[TextFileActor], fileIoMaker), "textFile")

  val expensesController = wire[ExpensesController]
}
