import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.math.BigDecimal
import scala.util.Try

import play.api.data.validation.ValidationError
import play.api.libs.json._

package object models {
  object IsLocalDate {
    def unapply(dateString: String): Option[String] = Try {
        LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        dateString
    }.toOption
  }

  implicit val localDateJsonWrites: Writes[LocalDate] = new Writes[LocalDate] {
    def writes(date: LocalDate): JsValue = {
      JsString(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
  }
  implicit val localDateJsonReads: Reads[LocalDate] = new Reads[LocalDate] {
    def reads(json: JsValue): JsResult[LocalDate] = json match {
      case JsString(aString) => aString match {
        case IsLocalDate(dateString) => JsSuccess(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE))
        case _ => JsError(ValidationError("error.expected.localdate"))
      }
      case _ => JsError(ValidationError("error.expected.jsstring"))
    }
  }

  implicit val bigDecimalJsonFormat: Format[BigDecimal] = new Format[BigDecimal] {
    def writes(number: BigDecimal): JsValue = {
      JsNumber(number)
    }

    def reads(json: JsValue): JsResult[BigDecimal] = json match {
      case JsNumber(number: BigDecimal) => JsSuccess(number)
      case _ => JsError(ValidationError("error.expected.jsnumber"))
    }
  }
}
