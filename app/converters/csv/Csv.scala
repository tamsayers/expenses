package converters.csv

object Csv {
  implicit class CsvSequence[T](sequence: Seq[T]) {
    def asCsv = sequence.mkString(",")
  }

  def toCsv[T](toConvert: T)(implicit csvString: CsvWriter[T]): String = csvString.write(toConvert)
}