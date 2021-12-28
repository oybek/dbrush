package io.github.oybek.dbrush.sql.postgres.syntax

import io.github.oybek.dbrush.sql.postgres.model.Action.Create
import io.github.oybek.dbrush.sql.postgres.model.Action.TableType.{GlobalTemporary, LocalTemporary}
import io.github.oybek.dbrush.sql.postgres.model.{Action, Column}

trait CreateSyntax {
  object create {
    object global { object temporary {
      def table(name: String)(columns: Column*): Action =
        Create(
          tableType = Some(GlobalTemporary),
          ifNotExists = false,
          tableName = name,
          columns = columns.toList
        )
    } }
    object local { object temporary {
      def table(name: String)(columns: Column*): Action =
        Create(
          tableType = Some(LocalTemporary),
          ifNotExists = false,
          tableName = name,
          columns = columns.toList
        )
    } }
    object ifNotExists {
      def table(name: String)(columns: Column*): Action =
        Create(
          ifNotExists = true,
          tableName = name,
          columns = columns.toList
        )
    }
    def table(name: String)(columns: Column*): Action =
      Create(
        ifNotExists = false,
        tableName = name,
        columns = columns.toList
      )
  }
}
