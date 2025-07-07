////> using dep com.lihaoyi::os-lib:0.11.4

import os.*
import io.circe.parser.*

object SimpleJsonRead {
  def main(args: Array[String]): Unit = {

    val root = os.pwd

    val path: os.Path = root / "src" / "main" / "resources" / "test.json"
    val file = os.read(path)
    
    println(parse(file).map(_.as[List[Result]]))
    
  }
}