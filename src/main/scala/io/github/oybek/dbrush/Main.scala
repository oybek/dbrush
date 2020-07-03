package io.github.oybek.dbrush

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.github.oybek.dbrush.syntax._
import io.github.oybek.dbrush.model.Migration

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {
  type F[+T] = IO[T]

  override def run(args: List[String]): IO[ExitCode] =
    for {
      logger <- Slf4jLogger.create[F]
      _ <- resources
        .use {
          transactor =>
            lazy val createCity =
              sql"""
                   |create table city(
                   |  name varchar,
                   |  population int not null
                   |)
                   |""".stripMargin

            lazy val createStudent =
              sql"""
                   |create table Student(
                   |  name varchar,
                   |  age int
                   |)
                   |""".stripMargin

            lazy val plugFuzzySearch =
              sql"CREATE EXTENSION fuzzystrmatch"

            List(
              Migration("create city", createCity),
              Migration("create student", createStudent),
              Migration("plug fuzzy search", plugFuzzySearch)
            ).exec(transactor, Some(logger.info(_: String)))
        }
    } yield ExitCode.Success

  private def resources(implicit ec: ExecutionContext): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost:5432/gdetram",
      "gdetram",
      "",
      ec,
      Blocker.liftExecutionContext(ec)
    )
}
