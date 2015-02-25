package repos

import java.io.File
import scala.io.Source
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import models.expenses.TestHelpers._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.test._
import java.time.LocalDate
import models.expenses.TestHelpers._
import scala.async.Async.async
import play.api.libs.json.Json
import org.mockito.MockSettings
import org.mockito.internal.creation.MockSettingsImpl

class JsonExpensesRepositorySpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  trait testRepo {
    val fileIo = mock[FileIO]
    val repo = new JsonExpensesRepository(fileIo)
  }

  "save" should {
    val exp = Seq(testExpense())
    val saveResult = async{}
    "add the expenses to the file" in new testRepo {
      when(fileIo.read()).thenReturn(async(""))
      when(fileIo.save(s"""[${defaultExpensesJson}]""")).thenReturn(saveResult)

      await(repo.save(exp)) mustBe ({})
    }
    "add to the existing expenses in the file" in new testRepo {
      when(fileIo.read()).thenReturn(async("[]"))
      when(fileIo.save(s"""[${defaultExpensesJson}]""")).thenReturn(saveResult)

      await(repo.save(exp)) mustBe ({})
    }
  }

  "for dates" should {
    val now = LocalDate.now()
    "read the expenses and return the ones for the specified date range" in new testRepo {
      val exp = Seq(
          testExpense(date = now.minusDays(10)),
          testExpense(date = now.minusDays(5)),
          testExpense(date = now),
          testExpense(date = now.plusDays(2)),
          testExpense(date = now.plusDays(3))
          )
      val expensesJson = Json.toJson(exp).toString
      when(fileIo.read).thenReturn(async(expensesJson))

      await(repo.forDates(testExpensesQuery(from = now.minusDays(5), till = now.plusDays(2)))) mustBe exp.slice(1, 4)
    }
    "read the expenses and return the ones for the specified supplier" in new testRepo {
      val exp = Seq(
          testExpense(supplier = "right-supplier", description = "desc1", date = now),
          testExpense(supplier = "right-supplier", description = "desc2", date = now),
          testExpense(description = "desc3", date = now),
          testExpense(supplier = "right-supplier", description = "desc4", date = now),
          testExpense(description = "desc5", date = now)
          )
      val expensesJson = Json.toJson(exp).toString
      when(fileIo.read).thenReturn(async(expensesJson))

      await(repo.forDates(testExpensesQuery(supplier = Some("right-supplier")))) mustBe Seq(exp(0), exp(1), exp(3))
    }
    "give an empty sequence for no file content"in new testRepo {
      when(fileIo.read).thenReturn(async(""))

      await(repo.forDates(testExpensesQuery(from = now.minusDays(5), till = now.plusDays(2)))) mustBe Nil
    }
  }
}
