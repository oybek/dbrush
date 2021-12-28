package io.github.oybek.dbrush.sql.postgres.syntax.ops

import io.github.oybek.dbrush.sql.postgres.model.Column

final class StringOps(val string: String) extends AnyVal {
  def ofType(dataType: String): Column =
    Column(
      name = string,
      dataType = dataType,
      collation = None,
      columnConstraint = Nil
    )

  def bigserial: Column = ofType("Bigserial")
  def bigint: Column = ofType("Bigint")
}
