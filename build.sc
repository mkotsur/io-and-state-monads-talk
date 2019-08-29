// build.sc
import mill._, scalalib._, scalafmt.ScalafmtModule

trait MyScalaModule extends ScalaModule {
  override val scalacOptions = List(
    "-language:higherKinds",
    "-Ypartial-unification",
  )
  
  override def scalaVersion = "2.12.8"

  override def scalacPluginIvyDeps = Agg(
    ivy"com.olegpy::better-monadic-for:0.3.0",
    ivy"org.typelevel::kind-projector:0.10.3",
  )
}

object cats extends MyScalaModule with ScalafmtModule {

  override def ivyDeps = Agg(
    ivy"org.typelevel::cats-effect::2.0.0-RC1",

  // ivy"org.typelevel::cats-mtl-core:0.4.0",
    // ivy"com.olegpy::meow-mtl:0.2.0",
    // ivy"io.github.timwspence::cats-stm:0.4.0",
    // ivy"co.fs2::fs2-io:1.0.4",
    // ivy"io.chrisdavenport::log4cats-slf4j:0.3.0",
    // ivy"com.github.julien-truffaut::monocle-core:${Versions.monocle}",
    // ivy"com.github.julien-truffaut::monocle-macro:${Versions.monocle}",
    // ivy"com.github.bigwheel::util-backports:1.1",
  )
}

object naive extends ScalaModule with MyScalaModule with ScalafmtModule