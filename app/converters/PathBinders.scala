package converters

import java.time.LocalDate
import play.api.mvc.PathBindable
import java.time.format.DateTimeFormatter
import scala.util._
import play.api.mvc.QueryStringBindable

object PathBinders {
  implicit val localDateBinder = new PathBindable[LocalDate] {
    def bind(key: String, value: String): Either[String, LocalDate] = Try(LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)) match {
      case Success(date) => Right(date)
      case Failure(e)    => Left(e.getMessage)
    }

    def unbind(key: String, value: LocalDate): String = value.format(DateTimeFormatter.ISO_LOCAL_DATE)
  }
}
