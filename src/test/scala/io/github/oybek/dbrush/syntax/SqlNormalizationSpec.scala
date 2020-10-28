package io.github.oybek.dbrush.syntax

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class SqlNormalizationSpec extends AnyFlatSpec with should.Matchers {

  "'\\n' '\\t' ' '" should "be whitespaces" in {
    "\n\t ".forall(_.isWhitespace) should be(true)
  }

  "extra whitespaces" should "be removed" in {
    SqlOps.normalizeQuery("  create     table foo (\nbar   varchar)") should be(
      "create table foo(bar varchar)"
    )
    SqlOps.normalizeQuery(
      "  create     table foo (\nbar   varchar , age int)"
    ) should be("create table foo(bar varchar,age int)")
    SqlOps.normalizeQuery("\n\t  select * from foo") should be(
      "select*from foo"
    )
    SqlOps.normalizeQuery(
      "update student set name = 'ji' where name = 'john'"
    ) should be("update student set name='ji'where name='john'")
  }

  "characters" should "be lowercased if possible" in {
    SqlOps.normalizeQuery("CREATE tAble Foo(bar VarChar)") should be(
      "create table foo(bar varchar)"
    )
    SqlOps.normalizeQuery("CREATE tAble \"Foo\"(bar VarChar)") should be(
      "create table\"Foo\"(bar varchar)"
    )
    SqlOps.normalizeQuery(
      "insert into Student (name) values ('John')"
    ) should be("insert into student(name)values('John')")
  }
}
