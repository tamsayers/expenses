import play.PlayImport.PlayKeys._
import com.typesafe.sbt.packager.docker._

name := "expenses-api"

dockerBaseImage := "java:8"

version := "1.0.0-SNAPSHOT"

lazy val fileIo = project

lazy val root = (project in file(".")).enablePlugins(PlayScala)
                                      .enablePlugins(DockerPlugin)
                                      .aggregate(fileIo)
                                      .dependsOn(fileIo)

scalaVersion := Common.scalaVersion

val dependencies = Seq(
  "com.softwaremill.macwire" % "macros_2.11" % "1.0.7",
  "org.scala-lang.modules" % "scala-async_2.11" % "0.9.3")

val testDependencies = Seq(
  "org.scalatestplus" % "play_2.11" % "1.2.0",
  "org.mockito" % "mockito-all" % "1.10.19").map(_ % "test")
  
libraryDependencies ++= dependencies ++ testDependencies ++ Common.testDependencies

routesImport += "converters.PathBinders._"

EclipseKeys.withSource in ThisBuild := true
