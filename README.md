# DB RUSH ![master](https://github.com/oybek/dbrush/workflows/master/badge.svg) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=oybek_db-rush&metric=ncloc)](https://sonarcloud.io/dashboard?id=oybek_db-rush) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=oybek_db-rush&metric=coverage)](https://sonarcloud.io/dashboard?id=oybek_db-rush) <a href="https://typelevel.org/cats/"><img src="https://user-images.githubusercontent.com/202410/45928046-6dbfd980-bf3d-11e8-8693-c1285dae03ce.png" align="right" alt="Cats friendly" /></a>

*Super simple pure migration tool for scala*

Traditional migration tools such as liquibase or flyway works with externally
described migration file.

**DB RUSH** is a simple library to do migrations right in scala code.

Import:
```scala
libraryDependencies ++= Seq(
  "io.github.oybek" % "dbrush" % "0.1"
)
```

Example:
```scala
import doobie._
import doobie.implicits._

lazy val createCity =
  sql"""
       |create table city(
       |  name varchar,
       |  population int not null
       |)
       |""".stripMargin

lazy val createSchool =
  sql"""
       |create table school(
       |  name varchar,
       |  latitude float,
       |  longitude float
       |)
       |""".stripMargin

lazy val createStudent =
  sql"""
       |create table Student(
       |  name varchar,
       |  age int
       |)
       |""".stripMargin

lazy val plugFuzzySearch =
  sql"CREATE EXTENSION fuzzystrmatch"

// Migration list
List(
  Migration("create city", createCity, createSchool),
  Migration("create student", createStudent),
  Migration("plug fuzzy search", plugFuzzySearch)
).exec[IO](transactor) // F[Unit]
```

Each migration executed within one transaction.
Only appends to migration list allowed, otherwise you'll get consistency
hash (md5) mismatch at migration phase.

Library creates `db_rush_commits` for usage:
```sql
test_db=# select * from db_rush_commits;
 index |       label       |               md5
-------+-------------------+----------------------------------
     1 | create city       | 6AD188238A2F3B19E0AE1FEBB7383992
     2 | create student    | 26E31E875DB55C8746728D6B789289BE
     3 | plug fuzzy search | 90B3A78FA8FE18CE36EB56F3C3A54494
(3 rows)
```

Tested under Postgresql:

* 10.[2-13]
* 9.6.12
