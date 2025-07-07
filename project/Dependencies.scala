import sbt.*

object Dependencies {
  object Version {
    val ScalaVersion = "3.4.2"
    val CatsEffect = "3.5.4"
    val Circe = "0.14.14"
    val CirceExtras = "0.14.1"
    val CirceRefined = "0.15.1"
    val Fs2 = "3.12.0"
    val Http4s = "0.23.27"
    val Refined     = "0.11.2"

    // TEST
    val Munit = "0.7.29"
    val MunitCatsEffect = "1.0.7"
    val MunitCatsEffectScalaCheck = "1.0.4"
    val Weaver = "0.8.4"
  }

  val CatsEffect = List(
    "org.typelevel" %% "cats-effect",
    "org.typelevel" %% "cats-effect-kernel",
    "org.typelevel" %% "cats-effect-std",
  ).map(_ % Version.CatsEffect)

  val Circe = List(
    "io.circe" %% "circe-core"
  ).map(_ % Version.Circe)

  val CirceExtras = List(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-refined",
    "io.circe" %% "circe-extras"
  ).map(_ % Version.CirceExtras)

  val Fs2 = List(
    "co.fs2" %% "fs2-core",
    "co.fs2" %% "fs2-io",
//    "co.fs2" %% "circe-fs2"
  ).map(_ % Version.Fs2)



  val Http4s = List(
    "org.http4s" %% "http4s-core",
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-circe",
    "org.http4s" %% "http4s-ember-server",
    "org.http4s" %% "http4s-ember-client",
    "org.http4s" %% "http4s-circe",
    "org.http4s" %% "http4s-dsl",
  ).map(_ % Version.Http4s)

  val Refined = List(
    "eu.timepit" %% "refined",
    "eu.timepit" %% "refined-cats"
  ).map(_ % Version.Refined)

  // TEST
  val MunitTest = List(
    "org.scalameta" %% "munit",
    "org.scalameta" %% "munit-scalacheck"
  ).map(_ % Version.Munit)

  val MunitCatsEffectScalaCheck = List("org.typelevel" %% "scalacheck-effect-munit" % Version.MunitCatsEffectScalaCheck)

  val MunitCatsEffect = List(
    "org.typelevel" %% "munit-cats-effect-3" % Version.MunitCatsEffect
  )

  val Weaver = List(
    "com.disneystreaming" %% "weaver-cats" % Version.Weaver,
    "com.disneystreaming" %% "weaver-scalacheck" % Version.Weaver
  )
}
