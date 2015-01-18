package repos

import org.scalatestplus.play.PlaySpec
import models.expenses.Expense
import play.api.test.FutureAwaits
import play.api.test.DefaultAwaitTimeout
import java.io.File
import scala.io.Source
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class JsonExpensesRepositorySpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  val expenses = Seq(Expense(value = 1.99))
  val expensesFile = File.createTempFile("expenses", ".json")

  trait testRepo {
    val fileServer = mock[FileServer]
    val repo = new JsonExpensesRepository(fileServer)
  }

  "expenses" should {
    "be added to the file" in new testRepo {
      when(fileServer.file("expenses.json")).thenReturn(expensesFile)

      await(repo.save(expenses))

      Source.fromFile(expensesFile).mkString mustBe """[{"value":1.99}]"""
    }
  }
}
