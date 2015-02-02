package repos

import scala.concurrent.Future

trait FileIO {
  def save(text: String): Future[Unit]

  def read(): Future[String]
}
