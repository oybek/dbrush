package io.github.oybek.dbrush.sql.postgres

import io.github.oybek.dbrush.sql.postgres.syntax._

object Example {
  create.table("balance")(
    "telegram_id".bigserial.primaryKey,
    "seconds".bigint.notNull
  )

  s"""
     |create global temporary table balance (
     |  telegram_id bigserial primary key,
     |  seconds bigint not null
     |)
     |""".stripMargin

}
