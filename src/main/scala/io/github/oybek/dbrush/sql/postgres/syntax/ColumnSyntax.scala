package io.github.oybek.dbrush.sql.postgres.syntax

import io.github.oybek.dbrush.sql.postgres.model.Column
import io.github.oybek.dbrush.sql.postgres.syntax.ops.{ColumnOps, StringOps}

trait ColumnSyntax {
  implicit final def toStringOps(string: String): StringOps = new StringOps(string)
  implicit final def toColumnOps(column: Column): ColumnOps = new ColumnOps(column)
}
