package models

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.libs.json.JsString
import java.time.LocalDate
import play.api.libs.json.JsNumber
import play.api.libs.json.JsError
import play.api.data.validation.ValidationError
import play.api.libs.json.JsPath

class packageSpec extends PlaySpec with MockitoSugar {
  "local date reads" should {
    "be implicitly parsed to local date from json string" in {
      JsString("2015-01-01").as[LocalDate] mustBe LocalDate.of(2015, 1, 1)
    }
    "give an error from a non json string" in {
      JsNumber(1).validate[LocalDate] mustBe an [JsError]
    }
    "give an error from an invalid date string" in {
      JsString("not a date").validate[LocalDate] mustBe JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.localdate"))))
    }
  }
}
