name := "file-io"

scalaVersion := Common.scalaVersion

val testDependencies = Seq(
    "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.4", 
    "org.scalatest" % "scalatest_2.11" % "2.2.3").map(_ % "test")

val compileDependencies = Seq("com.typesafe.akka" % "akka-actor_2.11" % "2.3.4")

libraryDependencies ++= compileDependencies ++ testDependencies
