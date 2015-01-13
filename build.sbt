name := "expenses-api"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.scalatestplus" %% "play" % "1.1.0" % "test", 
  "org.mockito" % "mockito-all" % "1.10.19" % "test")