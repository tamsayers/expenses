import com.softwaremill.macwire._
import controllers.ExpensesController
import services._
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
  val vatRate: BigDecimal = 0.2
  val ex: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
  lazy val actorSystem = ActorSystem("expenses")
  lazy val expensesFilePath = Paths.get("/var", "expenses", "expenses.json")
  lazy val fileIoMaker = FileIoActor.fileIoMakerFor(expensesFilePath)
  lazy val textFileActor: ActorRef = actorSystem.actorOf(Props(classOf[TextFileActor], fileIoMaker), "textFile")

  lazy val expensesController = wire[ExpensesController]
}

object Application extends Application
