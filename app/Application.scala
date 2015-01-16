import com.softwaremill.macwire.MacwireMacros._
import controllers.ExpensesController
import services._

object Application extends ServicesModule {
  val expensesController = wire[ExpensesController]
}