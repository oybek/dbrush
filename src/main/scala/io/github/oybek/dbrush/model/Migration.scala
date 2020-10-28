package io.github.oybek.dbrush.model

import java.math.BigInteger

import cats.implicits._
import cats.data.NonEmptyList
import doobie.Fragment
import io.github.oybek.dbrush.syntax.SqlOps._

final case class Migration(label: String, fr: Fragment, frs: Fragment*) {
  lazy val md5Hash: String = {
    val md5Hash = java.security.MessageDigest
      .getInstance("MD5")
      .digest(NonEmptyList.of(fr, frs: _*).mkString_(";").getBytes)
    val bigInt = new BigInteger(1, md5Hash)
    bigInt.toString(16).toUpperCase
  }
}
