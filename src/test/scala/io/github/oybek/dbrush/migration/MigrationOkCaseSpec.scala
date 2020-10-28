package io.github.oybek.dbrush.migration

import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import doobie.implicits._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.github.oybek.dbrush.model.Migration
import io.github.oybek.dbrush.syntax.MigrationsOps
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class MigrationOkCaseSpec extends Common {

  override def afterStart(): Unit = ()

  private val createCity =
    sql"""
         |create table city(
         |  name varchar,
         |  population int not null
         |)
         |""".stripMargin

  private val createStudent =
    sql"""
         |create table student(
         |  name varchar,
         |  age int
         |)
         |""".stripMargin

  test("when all migration success") {
    Slf4jLogger
      .create[IO]
      .flatMap { logger =>
        List(
          Migration("create city", createCity),
          Migration("create student", createStudent)
        ).exec(transactor, Some(logger.info(_: String)))
      }
      .attempt
      .unsafeRunSync() shouldBe ().asRight[Throwable]
  }
}
