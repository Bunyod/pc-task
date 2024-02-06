import sbt.*

object Dependencies {

  object Versions {
    val cats             = "2.10.0"
    val catsEffect       = "3.5.3"
    val logback          = "1.3.5"
    val refined          = "0.11.0"
    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.2"
    val fs2              = "3.9.3"
    val http4s           = "0.23.24"
    val circe            = "0.14.5"
    val pureConfig       = "0.17.5"
    val scalaCheck       = "1.17.0"
    val scalatest        = "3.2.17"
    val scalatestplus    = "3.2.17.0"

  }

  object Libraries {
    val cats       = "org.typelevel" %% "cats-core"   % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect

    def http4s(artifact: String): ModuleID = "org.http4s" %% s"http4s-$artifact" % Versions.http4s
    def circe(artifact: String): ModuleID  = "io.circe"   %% artifact            % Versions.circe

    val fs2Core = "co.fs2" %% "fs2-core" % Versions.fs2
    val fs2Io   = "co.fs2" %% "fs2-io"   % Versions.fs2

    val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfig

    val circeCore    = circe("circe-core")
    val circeGeneric = circe("circe-generic")
    val circeParser  = circe("circe-parser")
    val circeRefined = circe("circe-refined")

    val http4sDsl    = http4s("dsl")
    val http4sServer = http4s("ember-server")
    val http4sClient = http4s("ember-client")
    val http4sCirce  = http4s("circe")

    val refinedCore       = "eu.timepit" %% "refined"            % Versions.refined
    val refinedCats       = "eu.timepit" %% "refined-cats"       % Versions.refined
    val refinedPureconfig = "eu.timepit" %% "refined-pureconfig" % Versions.refined

    val logback = "ch.qos.logback" % "logback-classic" % Versions.logback % Runtime

    val scalaCheck    = "org.scalacheck"    %% "scalacheck"      % Versions.scalaCheck    % Test
    val scalatest     = "org.scalatest"     %% "scalatest"       % Versions.scalatest     % Test
    val scalatestplus = "org.scalatestplus" %% "scalacheck-1-17" % Versions.scalatestplus % Test

  }

  object CompilerPlugins {
    val betterMonadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor)
    val kindProjector    = compilerPlugin(
      ("org.typelevel" %% "kind-projector" % Versions.kindProjector).cross(CrossVersion.full)
    )
  }

  val common: Seq[ModuleID] = Seq(
    compilerPlugin(CompilerPlugins.kindProjector.cross(CrossVersion.full)),
    compilerPlugin(CompilerPlugins.betterMonadicFor),
    CompilerPlugins.kindProjector
  )

  val cats: Seq[ModuleID] = Seq(
    Libraries.cats,
    Libraries.catsEffect
  )

  val circe: Seq[ModuleID] = Seq(
    Libraries.circeCore,
    Libraries.circeGeneric,
    Libraries.circeParser,
    Libraries.circeRefined
  )

  val fs2: Seq[ModuleID] = Seq(
    Libraries.fs2Core,
    Libraries.fs2Io
  )

  val http4s: Seq[ModuleID] = Seq(
    Libraries.http4sDsl,
    Libraries.http4sClient,
    Libraries.http4sServer,
    Libraries.http4sCirce
  )

  val refined: Seq[ModuleID] = Seq(
    Libraries.refinedPureconfig,
    Libraries.refinedCore,
    Libraries.refinedCats
  )

  lazy val apiDependencies: Seq[ModuleID] = Seq(
    Libraries.pureConfig,
    Libraries.scalaCheck,
    Libraries.scalatest,
    Libraries.scalatestplus,
    Libraries.logback
  ) ++ common ++ cats ++ circe ++ http4s ++ refined

  lazy val integrationDependencies: Seq[ModuleID] = Seq(
    Libraries.scalaCheck,
    Libraries.scalatest,
    Libraries.scalatestplus
  ) ++ cats ++ http4s

}
