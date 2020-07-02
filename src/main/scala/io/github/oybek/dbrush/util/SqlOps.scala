package io.github.oybek.dbrush.util

import cats.Show
import doobie.Fragment

object SqlOps {
  // TODO: sql normalize
  implicit val showFragment: Show[Fragment] = Show.show(_.update.sql)
}
