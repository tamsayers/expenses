package converters.csv

import scala.reflect.ClassTag

trait CsvWriter[T] {
  def write(item: T): String
}
