package models.expenses

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import TestHelpers._

class CompanyCostSpec extends PlaySpec with MockitoSugar {
  "CsvWrites" should {
    val companyCosts = Seq(testCompanyCost(), testCompanyCost(amount = testAmount(vat = Some(0.33), details = Some("details"))))

    "write the header row" in {
      val csv = CompanyCost.companyCostsCsvWriter.write(companyCosts)

      csv.split("\n").head mustBe "date,description,client name,supplier,amount gross,amount net,amount vat,amount details"
    }

    "write a simple data row" in {
      val csv = CompanyCost.companyCostsCsvWriter.write(companyCosts)

      val expectedCost = companyCosts.head
      import expectedCost._
      csv.split("\n")(1) mustBe s"$date,$description,$clientName,$supplier,${amount.gross},${amount.net},${amount.vat},${amount.details}"
    }
  }
}