name := "file-io"

scalaVersion := Common.scalaVersion

val testDependencies = Seq("org.scalatest" % "scalatest_2.11" % "2.2.4") map(_ % "test")

val compileDependencies = Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.13",
  "com.typesafe" % "config" % "1.3.0"
  )

libraryDependencies ++= compileDependencies ++ testDependencies ++ Common.testDependencies

EclipseKeys.skipParents in ThisBuild := false

EclipseKeys.preTasks := Seq(compile in Compile)
