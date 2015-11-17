package loader

import com.softwaremill.macwire._
import controllers.ExpensesController
import services._
import akka.actor.ActorSystem
import akka.actor.ActorRef
import com.teck.fileio.TextFileActor
import akka.actor.Props
import com.teck.fileio.FileIoActor
import java.nio.file.Paths
import models.expenses.ExpenseRates
import com.teck.fileio.TextFileActor
import scala.math.BigDecimal.double2bigDecimal
import java.nio.file.Files

object Environment {
  trait ProductionComponents extends ServicesModule {
    lazy val expenseRates: ExpenseRates = ExpenseRates(vat = 0.2, mileage = 0.45)
    lazy val expensesActorSystem = ActorSystem("expenses")
    lazy val expensesFilePath = Paths.get("/var", "expenses", "expenses.json")
    lazy val fileIoMaker = FileIoActor.fileIoMakerFor(expensesFilePath)
    lazy val textFileActor: ActorRef = expensesActorSystem.actorOf(Props(classOf[TextFileActor], fileIoMaker), "textFile")

    lazy val expensesController = wire[ExpensesController]
  }
  
  trait TestComponents extends ProductionComponents {
    override lazy val expensesFilePath = Files.createTempFile("testExpenses", ".json")
  }

  trait DevComponents extends ProductionComponents {
    val userHome = System.getProperty("user.home")

    override lazy val expensesFilePath = Paths.get(userHome, "expenses", "expenses.json")
  }
}