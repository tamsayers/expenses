package converters

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import java.time.LocalDate

class PathBindersSpec extends PlaySpec with MockitoSugar {
  import PathBinders._

  "local date" should {
    "be bound to some Right local date if valid" in {
      localDateBinder.bind("date", "2015-01-01") mustBe Right(LocalDate.of(2015,1,1))
    }

    "be bound to some Left if invalid" in {
      val result = localDateBinder.bind("date", "not a date")
      result mustBe 'left
    }

    "be unbound" in {
      localDateBinder.unbind("", LocalDate.of(2015,2,3)) mustBe "2015-02-03"
    }
  }
}
