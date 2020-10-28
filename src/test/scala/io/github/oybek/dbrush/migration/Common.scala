package io.github.oybek.dbrush.migration

import cats.effect.IO
import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import doobie.scalatest.IOChecker
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import org.scalatest.funsuite.AnyFunSuite

trait Common extends AnyFunSuite with IOChecker with ForAllTestContainer {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  override val container = PostgreSQLContainer("postgres:10.13")
  lazy val transactor =
    Transactor
      .fromDriverManager[IO](
        container.driverClassName,
        container.jdbcUrl,
        container.username,
        container.password
      )
}
