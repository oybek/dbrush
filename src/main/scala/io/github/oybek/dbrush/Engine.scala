package io.github.oybek.dbrush

import cats.data.NonEmptyList
import cats.effect.Bracket
import cats.instances.list._
import cats.instances.option._
import cats.syntax.all._
import cats.{Applicative, MonadError, Show}
import doobie._
import doobie.implicits._
import io.github.oybek.dbrush.db.Queries
import io.github.oybek.dbrush.model.Migration

object Engine {

  implicit class MigrationsOps(val migrations: List[Migration]) extends AnyVal {
    def exec[F[_]: Applicative: Bracket[*[_], Throwable]](
        tx: Transactor[F],
        log: Option[String => F[Unit]] = None
    )(implicit F: MonadError[F, Throwable]): F[Unit] =
      for {
        _ <-
          log.traverse(_("DbRush - Create table if not exists db_rush_commits"))
        _ <- Queries.createCommitTable.run.transact(tx)
        _ <- log.traverse(_("DbRush - Loading commits"))
        commits <- Queries.selectCommit.to[List].transact(tx)
        _ <- log.traverse(_("DbRush - Sorting loaded commits by index"))
        commitsSorted = commits.sortBy(_.index)
        _ <-
          commitsSorted
            .map(Some(_))
            .zipAll(migrations.map(Some(_)), None, None)
            .traverse {
              case (Some(c), None) =>
                F.raiseError[Unit](
                  new Throwable(s"DbRush - No migration matching commit $c")
                )

              case (None, Some(m @ Migration(label, fr, frs @ _*))) =>
                log.traverse(_(s"DbRush - Applying migration '$label'")) *> (
                  Queries.insertCommit(label, m.md5Hash).run *>
                    NonEmptyList
                      .of(fr, frs: _*)
                      .map(_.update.run)
                      .reduceLeft(_ *> _)
                ).transact(tx).void

              case (Some(c), Some(m)) if c.label != m.label =>
                F.raiseError[Unit](
                  new Throwable(
                    s"DbRush - Label not matching got: '${m.label}' expected: '${c.label}'"
                  )
                )

              case (Some(c), Some(m)) if c.md5 != m.md5Hash =>
                F.raiseError[Unit](
                  new Throwable(
                    s"DbRush - Migration '${c.label}' md5 mismatch, got: '${m.md5Hash}' expected: '${c.md5}'"
                  )
                )

              case (Some(_), Some(m)) =>
                log
                  .traverse(_(s"DbRush - Skipping migration '${m.label}'"))
                  .void

              case _ => ().pure[F]
            }
        _ <- log.traverse(_("DbRush - Migration successful"))
      } yield ()
  }
}
