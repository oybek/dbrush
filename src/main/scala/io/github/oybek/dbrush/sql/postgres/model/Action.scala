package io.github.oybek.dbrush.sql.postgres.model

sealed trait Action
object Action {
  case class Create(tableType: Option[TableType] = None,
                    ifNotExists: Boolean,
                    tableName: String,
                    columns: List[Column],
                    parentTables: List[String] = Nil,
                    wizz: Option[With] = None,
                    onCommit: Option[OnCommit] = None,
                    tablespace: Option[String] = None) extends Action

  sealed trait TableType
  object TableType {
    case object GlobalTemporary extends TableType
    case object LocalTemporary extends TableType
  }

  sealed trait With
  object With {
    case class StorageParameters(storageParameter: (String, String)) extends With
    case object WithOids extends With
    case object WithoutOids extends With
  }

  sealed trait OnCommit
  object OnCommit {
    case object PreserveRows extends OnCommit
    case object DeleteRows extends OnCommit
    case object Drop extends OnCommit
  }
}
