package io.github.oybek.dbrush.db

import cats.effect.IO
import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import doobie.scalatest.IOChecker
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import io.github.oybek.dbrush.Engine._
import io.github.oybek.dbrush.model.Migration
import org.scalatest.funsuite.AnyFunSuite

class QueriesSpec extends AnyFunSuite with IOChecker with ForAllTestContainer {

  override val container = PostgreSQLContainer()
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  lazy val transactor =
    Transactor
      .fromDriverManager[IO](
        container.driverClassName,
        container.jdbcUrl,
        container.username,
        container.password
      )

  override def afterStart(): Unit =
    List[Migration]().exec(transactor).unsafeRunSync()

  test("check migration queries") {
    check(Queries.createCommitTable)
    check(Queries.selectCommit)
    check(Queries.insertCommit("foo", "bar"))
  }
}
