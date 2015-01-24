package repos

import org.scalatestplus.play.PlaySpec
import models.expenses.Expense
import models.expenses.TestHelpers._
import play.api.test.FutureAwaits
import play.api.test.DefaultAwaitTimeout
import java.io.File
import scala.io.Source
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import java.io.PrintWriter
import java.time.LocalDate

class JsonExpensesRepositorySpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  val expenses = Seq(testExpense(value = 1.99, LocalDate.of(2015, 1, 24)))
  val expensesFile = File.createTempFile("expenses", ".json")

  trait testRepo {
    val fileServer = mock[FileServer]
    val repo = new JsonExpensesRepository(fileServer)

    val pw = new PrintWriter(expensesFile)
    try {
      pw.print("[]")
    } finally {
      pw.close()
    }
  }

  "save" should {
    "add the expenses to the file" in new testRepo {
      when(fileServer.file("expenses.json")).thenReturn(expensesFile)

      await(repo.save(expenses))

      Source.fromFile(expensesFile).mkString mustBe """[{"value":1.99,"date":"2015-01-24"}]"""
    }
  }

  "for dates" should {
    "read the expenses and return the ones for the specified date range" in new testRepo {

    }
  }
}
