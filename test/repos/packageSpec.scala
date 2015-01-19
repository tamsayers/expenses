package repos

import org.scalatestplus.play.PlaySpec
import java.io.File

class packageSpec extends PlaySpec {
  val testDataFile = new File(getClass().getResource("/resources/test.txt").getFile)
  val testOutput = File.createTempFile("fileWriteTest", ".txt")

  "BetterFile" should {
    "read text from a file" in {
      testDataFile.text mustBe """|my
                              |test
                              |text""".stripMargin
    }

    "write text to a file" in {
      val testText = "test text"

      testOutput.text_=(testText)

      testOutput.text mustBe testText
    }
  }
}
