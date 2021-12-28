package io.github.oybek.dbrush.sql.postgres

import doobie.Fragment
import doobie.implicits.toSqlInterpolator
import io.github.oybek.dbrush.compiler.Compiler
import io.github.oybek.dbrush.compiler.syntax.toCompilerOps
import io.github.oybek.dbrush.sql.postgres.model.{Action, Column}
import io.github.oybek.dbrush.sql.postgres.model.Action.{Create, TableType}
import io.github.oybek.dbrush.sql.postgres.model.Action.TableType.{GlobalTemporary, LocalTemporary}

object Doobie {

  implicit val compiler: Compiler[Action, Fragment] = {
    case Create(
      tableType,
      ifNotExists,
      tableName,
      columns,
      parentTables,
      wizz,
      onCommit,
      tablespace
    ) =>
      List(
        fr"create",
        tableType.compile,
        if (ifNotExists) fr"if not exists" else Fragment.empty,
        Fragment.const(tableName),
        columns.compile
      ).reduce(_ ++ _)

    case _ => ???
  }

  // sub expression compilers

  implicit val tableTypeCompiler: Compiler[TableType, Fragment] = {
    case GlobalTemporary => fr"global temporary"
    case LocalTemporary => fr"local temporary"
  }

  implicit val columnsCompiler: Compiler[List[Column], Fragment] =
    columns =>
      columns.map {
        case Column(name, dataType, collation, columnConstraint) =>
          Fragment.const(name) ++
          Fragment.const(dataType) ++
          collation.fold(Fragment.empty)(fr"collate" ++ Fragment.const(_))
      } match {
        case Nil => Fragment.empty
        case frs => frs.reduce(_ ++ fr"," ++ _)
      }

  // Generic compilers (used for derivation)

  implicit def optionCompiler[A](implicit compiler: Compiler[A, Fragment]): Compiler[Option[A], Fragment] = {
    case Some(x) => x.compile
    case None => Fragment.empty
  }

  implicit def listCompiler[A](implicit compiler: Compiler[A, Fragment]): Compiler[List[A], Fragment] =
    _.map(_.compile).foldLeft(Fragment.empty)(_ ++ _)
}
