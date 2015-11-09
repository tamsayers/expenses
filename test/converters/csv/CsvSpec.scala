package converters.csv

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

class CsvSpec extends PlaySpec with MockitoSugar {
  import Csv._
  
  "to csv" should {
    "use the implicit CsvWriter to convert the given value" in {
      implicit val csvWriter = new CsvWriter[Int] {
        def write(item: Int): String = item.toString()
      }
      
      toCsv(1) mustBe "1"
    }
  }
}