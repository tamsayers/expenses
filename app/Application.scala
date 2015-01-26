import com.softwaremill.macwire.MacwireMacros._
import controllers.ExpensesController
import services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.async.Async.async

object Application extends ServicesModule {
  val fileIo = new repos.FileIO {
    var text = ""
    def save(text: String): Future[Unit] = async {
      this.text = text
    }

    def read: Future[String] = async(text)
  }

  val expensesController = wire[ExpensesController]
}
