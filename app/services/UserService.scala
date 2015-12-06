package services

import models.user.User
import scala.concurrent.Future

trait UserService {
  def forName(userName: String): Future[Option[User]]
}