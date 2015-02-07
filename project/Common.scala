import sbt._
import Keys._

object Common {
  val scalaVersion = "2.11.4"
  
  val testDependencies = Seq(
    "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.4").map(_ % "test")

}