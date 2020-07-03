package io.github.oybek

import cats.MonadError
import cats.effect.Bracket

package object dbrush {
  type MonadThrow[F[_]] = MonadError[F, Throwable]
  type BracketThrow[F[_]] = Bracket[F, Throwable]

}
