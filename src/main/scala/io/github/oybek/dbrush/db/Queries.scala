package io.github.oybek.dbrush.db

import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0
import io.github.oybek.dbrush.model.Commit

object Queries {
  val createCommitTable: Update0 =
    sql"""
         |create table if not exists
         |  db_rush_commits(
         |    index serial,
         |    label varchar not null,
         |    md5 varchar(32) not null
         |  )
         |""".stripMargin.update

  val selectCommit: Query0[Commit] =
    sql"select index, label, md5 from db_rush_commits".query[Commit]

  def insertCommit(label: String, md5: String): Update0 =
    sql"insert into db_rush_commits (label, md5) values ($label, $md5)".update
}
