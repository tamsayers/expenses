import java.nio.file.Paths
import java.nio.file.Files

object TestApplication extends Application {
  override lazy val expensesFilePath = Files.createTempFile("testExpenses", ".json")
}
