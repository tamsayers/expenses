name := "expenses-api"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

val dependencies = Seq(
  "com.softwaremill.macwire" % "macros_2.11" % "0.7.3")

val testDependencies = Seq(
  "org.scalatestplus" %% "play" % "1.1.0", 
  "org.mockito" % "mockito-all" % "1.10.19").map(_ % "test")
  
libraryDependencies ++= dependencies ++ testDependencies