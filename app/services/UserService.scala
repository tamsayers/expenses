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
    Map("test.user" -> User("test.user", "key", "4290bac24d78e756163d8de9db5d6dc15c1da845")).get(userName)
  }
}