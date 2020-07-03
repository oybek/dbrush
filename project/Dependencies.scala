import sbt._

object Dependencies {

  object V {
    val catsCore = "2.0.0"
    val catsEffect = "2.0.0"
    val scalaTest = "3.2.0"
    val slf4j = "1.7.26"
    val logback = "1.2.3"
    val doobie = "0.8.8"
    val mock = "4.4.0"
    val mockTest = "3.1.0"
  }

  val catsCore = "org.typelevel" %% "cats-core" % V.catsCore
  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
  val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % V.doobie,
    "org.tpolecat" %% "doobie-postgres" % V.doobie,
    "org.tpolecat" %% "doobie-hikari" % V.doobie,
    "org.tpolecat" %% "doobie-h2" % V.doobie,
    "org.tpolecat" %% "doobie-scalatest" % V.doobie % Test
  )

  val logger = Seq(
    "org.slf4j" % "slf4j-api" % V.slf4j,
    "ch.qos.logback" % "logback-classic" % V.logback,
    "io.chrisdavenport" %% "log4cats-slf4j" % "1.1.1"
  )

  val mock = Seq(
    "org.scalamock" %% "scalamock" % V.mock % Test,
    "org.scalatest" %% "scalatest" % V.mockTest % Test
  )

  val testContainers = Seq(
    "com.dimafeng" %% "testcontainers-scala-core" % "0.37.0" % "test",
    "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.37.0" % "test",
    "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.37.0" % "test"
  )

  val common = Seq(catsCore, catsEffect, scalaTest) ++
    logger ++
    doobie ++
    mock ++
    testContainers
}
