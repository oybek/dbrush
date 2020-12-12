lazy val gdetram = (project in file("."))
  .settings(
    name := "dbrush",
    version := "0.1",
    organization := "io.github.oybek",
    homepage := Some(url("https://github.com/oybek/dbrush")),
    scmInfo := Some(ScmInfo(url("https://github.com/oybek/dbrush"), "git@github.com:oybek/dbrush.git")),
    developers := List(
      Developer(
        id = "oybek",
        name = "oybek",
        email = "aybek.hashimov@gmail.com",
        url("https://oybek.github.io")
      )
    ),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    publishMavenStyle := true,
    crossPaths := false,

    libraryDependencies ++= Dependencies.common,
    sonarProperties := Sonar.properties,
    Compiler.settings,

    // add sonatype repository settings
    // snapshot versions publish to sonatype snapshot repository
    // other versions publish to sonatype staging repository
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    )
  )
