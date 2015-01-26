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
import models.expenses.DateQuery
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
    "add the expenses to the file" in new testRepo {
      val exp = Seq(testExpense())
      val saveResult = async{}

      when(fileIo.read()).thenReturn(async("[]"))
      when(fileIo.save(s"""[${defaultExpensesJson}]""")).thenReturn(saveResult)

      await(repo.save(exp)) mustBe ({})
    }
  }

  "for dates" should {
    "read the expenses and return the ones for the specified date range" in new testRepo {
      val now = LocalDate.now()
      val exp = Seq(
          testExpense(date = now.minusDays(10)),
          testExpense(date = now.minusDays(5)),
          testExpense(date = now),
          testExpense(date = now.plusDays(2)),
          testExpense(date = now.plusDays(3))
          )
      val expensesJson = Json.toJson(exp).toString
      when(fileIo.read).thenReturn(async(expensesJson))

      await(repo.forDates(DateQuery(from = now.minusDays(5), till = now.plusDays(2)))) mustBe exp.slice(1, 4)
    }
  }
}
