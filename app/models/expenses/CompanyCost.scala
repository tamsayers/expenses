package models.expenses

import java.time.LocalDate
import play.api.libs.json._
import play.api.libs.functional.syntax._
import converters.csv.CsvWriter
import converters.csv.Csv

case class CompanyCost(date: LocalDate, description: String, clientName: String, supplier: String, amount: Amount)

object CompanyCost {
  import models._
  import Amount.amountFormat

  implicit val companyCostFormat = Json.format[CompanyCost]

  private val csvFields = Seq("date","description","client name","supplier","amount gross","amount net","amount vat","amount details")

  private implicit val companyCostCsvWriter = new CsvWriter[CompanyCost] {
    def write(costs: CompanyCost): String = {
      import costs._
      Seq(date,description,clientName,supplier,amount.gross,amount.net,amount.vat,amount.details).mkString(",")
    }
  }

  implicit val companyCostsCsvWriter = new CsvWriter[Seq[CompanyCost]] {
    val headers = csvFields.mkString(",")
    def write(costs: Seq[CompanyCost]): String = headers :: costs.foldRight(List[String]()) { (cost, list) =>
        Csv.toCsv(cost) :: list
      } mkString("\n")
  }
}
