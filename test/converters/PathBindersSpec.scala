package converters

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import java.time.LocalDate

class PathBindersSpec extends PlaySpec with MockitoSugar {
  import PathBinders._

  "local date" should {
    "be bound to some Right local date if valid" in {
      localDateBinder.bind("date", Map("date" -> Seq("2015-01-01"))) mustBe Some(Right(LocalDate.of(2015,1,1)))
    }

    "be bound to some Left if invalid" in {
      val result = localDateBinder.bind("date", Map("date" -> Seq("not a date")))
      result mustBe 'defined
      result.get mustBe 'left
    }

    "be bound to none if param not found" in {
      localDateBinder.bind("date", Map()) mustBe None
    }

    "be unbound" in {
      localDateBinder.unbind("", LocalDate.of(2015,2,3)) mustBe "2015-02-03"
    }
  }
}
