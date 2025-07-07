import sbt.Keys.libraryDependencies

import Dependencies.*
import scala.collection.immutable.List

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := Version.ScalaVersion
ThisBuild / version      := "1.0.0"



lazy val root = (project in file("."))
  .settings(
    name := "football-radar-test",
      libraryDependencies ++= List.concat(
        CatsEffect,
        Circe,
        CirceExtras,
        Fs2,
        List("org.gnieh" %% "fs2-data-json-circe" % "1.12.0"),
        List("com.lihaoyi" %% "os-lib" % "0.11.4")
      )
  )

