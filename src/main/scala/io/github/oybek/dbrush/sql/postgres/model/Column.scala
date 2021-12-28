package io.github.oybek.dbrush.sql.postgres.model

case class Column(name: String,
                  dataType: String,
                  collation: Option[String],
                  columnConstraint: List[ColumnConstraint])
