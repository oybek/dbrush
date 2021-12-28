package io.github.oybek.dbrush.sql.postgres

import io.github.oybek.dbrush.compiler.syntax.toCompilerOps
import io.github.oybek.dbrush.sql.postgres.Doobie.compiler
import io.github.oybek.dbrush.sql.postgres.syntax._

object Example {
  create.ifNotExists.table("balance")(
    "telegram_id".bigserial.primaryKey,
    "seconds".bigint.notNull
  ).compile

  s"""
     |create global temporary table balance (
     |  telegram_id bigserial primary key,
     |  seconds bigint not null
     |)
     |""".stripMargin

}
