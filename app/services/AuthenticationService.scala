package services

import scala.concurrent.Future

trait AuthenticationService {
  def authorize(username: String, password: String): Future[Option[String]]
}
