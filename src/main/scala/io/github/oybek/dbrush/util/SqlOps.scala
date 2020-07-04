package io.github.oybek.dbrush.util

import cats.Show
import doobie.Fragment

object SqlOps {
  // TODO: sql normalize
  implicit val showFragment: Show[Fragment] = Show.show(
    ((x: Fragment) => x.update.sql).andThen(normalizeQuery)
  )

  def normalizeQuery(query: String): String = {
    normalizeSingleQuery(query)
  }

  private def normalizeSingleQuery(query: String): String =
    query
      .foldLeft((0, new StringBuilder(query.length))) {
        case ((0, s), c) if s.isEmpty && (c.isWhitespace || c == ';') => 0 -> s
        case ((0, s), c) if c.isWhitespace && s.last.isWhitespace     => 0 -> s
        case ((0, s), c) if c.isWhitespace && s.last.oneOf("(),*=;")  => 0 -> s
        case ((0, s), c) if c.oneOf("(),*=;") && s.last.isWhitespace =>
          0 -> s.updateLast(c)
        case ((0, s), c) if c.oneOf("\"'") =>
          1 -> (if (s.last.isWhitespace) s.updateLast(c) else s += c)
        case ((1, s), c) if c.oneOf("\"'")                        => 0 -> (s += c)
        case ((0, s), c) if c.isWhitespace && s.last.oneOf("\"'") => 0 -> s
        case ((1, s), c)                                          => 1 -> (s += c)
        case ((0, s), c)                                          => 0 -> (s += c.toLower)
      }
      ._2
      .toString

  implicit class CharOps(val c: Char) extends AnyVal {
    def oneOf(s: String): Boolean =
      s.contains(c)
  }

  implicit class StringBuilderOps(val sb: StringBuilder) extends AnyVal {
    def updateLast(c: Char): StringBuilder =
      sb.setCharAt(sb.length - 1, c)
  }
}
