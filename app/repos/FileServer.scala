package repos

import java.io.File

trait FileServer {
  def file(path: String): File
}
