package io.github.oybek.dbrush.db

import cats.effect.IO
import com.dimafeng.testcontainers.{
  Container,
  ForAllTestContainer,
  PostgreSQLContainer
}
import doobie.scalatest.IOChecker
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import io.github.oybek.dbrush.Engine._
import io.github.oybek.dbrush.model.Migration
import org.scalatest.funsuite.AnyFunSuite

trait QueriesSpec extends AnyFunSuite with IOChecker with ForAllTestContainer {
  def container: PostgreSQLContainer
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

class `PG_10_13` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.13")
}
class `PG_10_12` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.12")
}
class `PG_10_11` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.11")
}
class `PG_10_10` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.10")
}
class `PG_10_9` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.9")
}
class `PG_10_8` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.8")
}
class `PG_10_7` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.7")
}
class `PG_10_6` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.6")
}
class `PG_10_5` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.5")
}
class `PG_10_4` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.4")
}
class `PG_10_3` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.3")
}
class `PG_10_2` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:10.2")
}

class `PG_9_6_12` extends QueriesSpec {
  override val container = PostgreSQLContainer("postgres:9.6.12")
}
