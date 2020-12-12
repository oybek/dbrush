package io.github.oybek.dbrush

import cats.data.NonEmptyList
import cats.implicits._
import doobie._
import doobie.implicits._
import io.github.oybek.dbrush.db.Queries
import io.github.oybek.dbrush.model.{Commit, Migration}

/**
  * Allows to execute desired migrations
  */
trait MigrationExecutor[F[_]] {
  def apply(migrations: List[Migration]): F[Unit]
}

object MigrationExecutor {
  def make[F[_]: BracketThrow](
      tx: Transactor[F],
      log: Option[String => F[Unit]] = None
  ): MigrationExecutor[F] =
    new MigrationExecutor[F] {
      override def apply(migrations: List[Migration]): F[Unit] =
        for {
          _ <- log.traverse_(
            _("DbRush - Create table if not exists db_rush_commits")
          )
          _ <- Queries.createCommitTable.run.transact(tx)
          _ <- log.traverse_(_("DbRush - Loading commits"))
          commits <- Queries.selectCommit.to[List].transact(tx)
          _ <- log.traverse_(_("DbRush - Sorting loaded commits by index"))
          sortedCommits = commits.sortBy(_.index)
          _ <-
            sortedCommits
              .map(Some(_))
              .zipAll(migrations.map(Some(_)), None, None)
              .traverse_(cases)
          _ <- log.traverse_(_("DbRush - Migration successful"))
        } yield ()

      private val cases: ((Option[Commit], Option[Migration])) => F[Unit] = {
        case (Some(c), None) =>
          fail(s"No migration matching commit $c")

        case (None, Some(m @ Migration(label, fr, frs @ _*))) =>
          log.traverse(_(s"DbRush - Applying migration '$label'")) >> (
            Queries.insertCommit(label, m.md5Hash).run >>
              NonEmptyList
                .of(fr, frs: _*)
                .map(_.update.run)
                .reduceLeft(_ >> _)
          ).transact(tx).void

        case (Some(c), Some(m)) if c.label != m.label =>
          fail(s"Label not matching got: '${m.label}' expected: '${c.label}'")

        case (Some(c), Some(m)) if c.md5 != m.md5Hash =>
          fail(
            s"Migration '${c.label}' md5 mismatch, got: '${m.md5Hash}' expected: '${c.md5}'"
          )

        case (Some(_), Some(m)) =>
          log.traverse_(_(s"DbRush - Skipping migration '${m.label}'"))

        case _ => ().pure[F]
      }

      def fail(message: String): F[Unit] =
        new Throwable(s"DbRush - ERROR - $message").raiseError[F, Unit]
    }
}
