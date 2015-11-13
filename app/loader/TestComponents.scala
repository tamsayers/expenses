package loader

import java.nio.file.Files

trait TestComponents extends ProdComponents {
  override lazy val expensesFilePath = Files.createTempFile("testExpenses", ".json")
}

object TestComponents extends TestComponents