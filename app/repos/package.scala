import scala.concurrent.ExecutionContext
import java.io.File
import scala.io.Source
import java.io.PrintWriter

package object repos {
  import com.softwaremill.macwire.MacwireMacros._

  trait ReposModule {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    val fileIo: FileIO
    val expensesRepository = wire[JsonExpensesRepository]
  }

  implicit class TextFile(file: File) {
    def text: String = Source.fromFile(file).mkString

    def text_=(text: String): Unit = {
      val pw = new PrintWriter(file)
      try {
        pw.print(text)
      } finally {
        pw.close()
      }
    }
  }
}
