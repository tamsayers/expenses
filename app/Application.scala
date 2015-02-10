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

trait Application extends ServicesModule {
  lazy val actorSystem = ActorSystem("expenses")

  lazy val userHome = System.getProperty("user.home")
  lazy val expensesFilePath = Paths.get(userHome, "expenses.json")
  lazy val fileIoMaker = FileIoActor.fileIoMakerFor(expensesFilePath)
  lazy val textFileActor: ActorRef = actorSystem.actorOf(Props(classOf[TextFileActor], fileIoMaker), "textFile")

  lazy val expensesController = wire[ExpensesController]
}

object Application extends Application
