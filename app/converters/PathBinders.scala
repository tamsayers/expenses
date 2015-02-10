package converters

import java.time.LocalDate
import play.api.mvc.PathBindable
import java.time.format.DateTimeFormatter
import scala.util._
import play.api.mvc.QueryStringBindable

object PathBinders {
  implicit val localDateBinder = new QueryStringBindable[LocalDate] {
    def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDate]] = params.get(key).map { values =>
      Try(LocalDate.parse(values(0), DateTimeFormatter.ISO_LOCAL_DATE)) match {
        case Success(date) => Right(date)
        case Failure(e)    => Left(e.getMessage)
      }
    }

    def unbind(key: String, value: LocalDate): String = value.format(DateTimeFormatter.ISO_LOCAL_DATE)
  }
}
