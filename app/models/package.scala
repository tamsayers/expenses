import java.time.format.DateTimeFormatter
import java.time.LocalDate
import play.api.data.validation.ValidationError
import play.api.libs.json._

package object models {
  implicit val localDateJsonWrites: Writes[LocalDate] = new Writes[LocalDate] {
    def writes(date: LocalDate): JsValue = {
      JsString(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
  }
  implicit val localDateJsonReads: Reads[LocalDate] = new Reads[LocalDate] {
    def reads(json: JsValue): JsResult[LocalDate] = json match {
      case JsString(dateString) => JsSuccess(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE))
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
    }
  }
}
