package io.github.oybek.dbrush.model

import java.math.BigInteger
import doobie.Fragment

final case class Migration(label: String, fr: Fragment, frs: Fragment*) {
  lazy val md5Hash: String = {
    val md5Hash = java.security.MessageDigest
      .getInstance("MD5")
      .digest((fr +: frs).map(_.query.sql.trim).mkString(";").getBytes)
    val bigInt = new BigInteger(1, md5Hash)
    bigInt.toString(16).toUpperCase
  }
}
