package io.github.oybek.dbrush.sql.postgres.syntax.ops

import io.github.oybek.dbrush.sql.postgres.model.ColumnConstraint.ConstraintType
import io.github.oybek.dbrush.sql.postgres.model.ColumnConstraint.ConstraintType.{NotNull, PrimaryKey}
import io.github.oybek.dbrush.sql.postgres.model.{Column, ColumnConstraint}

class ColumnOps(val column: Column) extends AnyVal {
  def primaryKey: Column = constraint(PrimaryKey(""))
  def notNull: Column = constraint(NotNull)

  def collate(collation: String): Column =
    column.copy(collation = Some(collation))

  def constraint(constraintType: ConstraintType): Column =
    constraint(None, constraintType)

  def constraint(name: String, constraintType: ConstraintType): Column =
    constraint(Some(name), constraintType)

  private def constraint(name: Option[String], constraintType: ConstraintType): Column =
    column.copy(
      columnConstraint = column.columnConstraint :+ ColumnConstraint(
        name = name,
        constraintType = constraintType,
        defer = None,
        initDefer = None
      )
    )
}

