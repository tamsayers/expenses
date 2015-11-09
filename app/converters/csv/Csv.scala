package converters.csv

object Csv {
  def toCsv[T](toConvert: T)(implicit csvString: CsvWriter[T]): String = csvString.write(toConvert)
}