
ThisBuild / version := "0.1"
ThisBuild / organization := "io.github.oybek"

lazy val gdetram = (project in file("."))
  .settings(name := "dbrush")
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(sonarProperties := Sonar.properties)
  .settings(Compiler.settings)
