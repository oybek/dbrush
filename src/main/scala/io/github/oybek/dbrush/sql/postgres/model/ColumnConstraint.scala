package io.github.oybek.dbrush.sql.postgres.model

import io.github.oybek.dbrush.sql.postgres.model.ColumnConstraint.{ConstraintType, Defer, InitDefer}

case class ColumnConstraint(name: Option[String],
                            constraintType: ConstraintType,
                            defer: Option[Defer],
                            initDefer: Option[InitDefer])

object ColumnConstraint {
  sealed trait ConstraintType
  object ConstraintType {
    case object NotNull extends ConstraintType
    case object Null extends ConstraintType
    case class Check(expr: String) extends ConstraintType
    case class Default(expr: String) extends ConstraintType
    case class Unique(indexParameters: String) extends ConstraintType
    case class PrimaryKey(indexParameters: String) extends ConstraintType
    case class References(refTable: String,
                          refColumn: String,
                          matchType: Option[MatchType],
                          onDelete: Option[Action],
                          onUpdate: Option[Action]) extends ConstraintType
  }

  sealed trait MatchType
  object MatchType {
    case object MatchNull extends MatchType
    case object MatchPartial extends MatchType
    case object MatchSimple extends MatchType
  }

  sealed trait Defer
  object Defer {
    case object Deferrable extends Defer
    case object NotDeferrable extends Defer
  }

  sealed trait InitDefer
  object InitDefer {
    case object InitiallyDeferred extends InitDefer
    case object InitiallyImmediate extends InitDefer
  }
}
