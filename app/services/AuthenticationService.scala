package services

import scala.concurrent.Future
import models.auth.Authentication

trait AuthenticationService {
  def authorize(username: String, password: String): Future[Authentication]
}
