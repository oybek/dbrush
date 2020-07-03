package io.github.oybek.dbrush

import doobie.Transactor
import io.github.oybek.dbrush.model.Migration

object syntax {

  implicit class MigrationsOps(val migrations: List[Migration]) extends AnyVal {
    def exec[F[_]: BracketThrow](
        tx: Transactor[F],
        log: Option[String => F[Unit]] = None
    ): F[Unit] = MigrationExecutor.make[F](tx, log).apply(migrations)
  }

}
