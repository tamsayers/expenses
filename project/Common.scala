import sbt._
import Keys._

object Common {
  val scalaVersion = "2.11.7"
  
  val testDependencies = Seq(
    "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.13").map(_ % "test")
}