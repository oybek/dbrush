package io.github.oybek.dbrush.util

import cats.Show
import doobie.Fragment

object SqlOps {
  // TODO: sql normalize
  implicit val showFragment: Show[Fragment] = Show.show(_.update.sql)

  def normalizeQuery(query: String): String = {
    normalizeSingleQuery(query)
  }

  private def normalizeSingleQuery(query: String): String = {
    query.foldLeft(new StringBuilder(query.length)) {
      case (s, c) if s.isEmpty && (c.isWhitespace || c == ';') => s
      case (s, c) if s.isEmpty => s += c
      case (s, c) if c.isWhitespace && s.last.isWhitespace => s
      case (s, c) if c.isWhitespace && s.last.oneOf("(),*") => s
      case (s, c) if c.oneOf("(),*") && s.last.isWhitespace  => s.setCharAt(s.length-1, c)
      case (s, c) => s += c
    }.toString
  }

  implicit class CharOps(val c: Char) extends AnyVal {
    def oneOf(s: String, p: (Char => Boolean)*): Boolean =
      s.contains(c) && p.forall(_(c))
  }
}
