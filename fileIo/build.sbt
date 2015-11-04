name := "file-io"

scalaVersion := Common.scalaVersion

val testDependencies = Seq("org.scalatest" % "scalatest_2.11" % "2.2.3") map(_ % "test")

val compileDependencies = Seq("com.typesafe.akka" % "akka-actor_2.11" % "2.3.4")

libraryDependencies ++= compileDependencies ++ testDependencies ++ Common.testDependencies

EclipseKeys.skipParents in ThisBuild := false

EclipseKeys.withSource := true
