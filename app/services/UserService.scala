package services

import models.user.User
import scala.concurrent.Future
import scala.async.Async
import scala.concurrent.ExecutionContext

trait UserService {
  def forName(userName: String): Future[Option[User]]
}

class StubUserService(implicit val ex: ExecutionContext)  extends UserService {
  def forName(userName: String): Future[Option[User]] = Async.async {
    Map("name" -> User("name", "key", "hash")).get(userName)
  }
}