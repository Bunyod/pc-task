ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / scalacOptions ++= CompilerOptions.cOptions

lazy val root = (project in file("."))
  .settings(name := "pc-task")
  .aggregate(`word-counter-api`, integration)
  .settings(addCommandAlias("run", "word-counter-api/run"))

lazy val `word-counter-api` = (project in file("word-counter-api"))
  .settings(
    Compile / run / fork := true,
    libraryDependencies ++= Dependencies.apiDependencies
  )

lazy val integration = (project in file("integration"))
  .dependsOn(`word-counter-api`)
  .settings(
    libraryDependencies ++= Dependencies.integrationDependencies
  )
