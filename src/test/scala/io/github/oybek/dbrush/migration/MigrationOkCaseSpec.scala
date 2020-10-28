package io.github.oybek.dbrush.migration

import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import doobie.implicits._
import io.github.oybek.dbrush.model.Migration
import io.github.oybek.dbrush.syntax.MigrationsOps
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.collection.mutable.ArrayBuffer

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
    val logs = new ArrayBuffer[String]()
    val logger = Some((x: String) => IO { logs.addOne(x); () })

    List(
      Migration("create city", createCity),
      Migration("create student", createStudent)
    )
      .exec[IO](transactor, logger)
      .attempt
      .unsafeRunSync() shouldBe ().asRight[Throwable]
  }
}
