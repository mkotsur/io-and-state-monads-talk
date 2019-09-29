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
    ivy"org.typelevel::cats-effect::2.0.0-RC1"
  )

  def run3() = T.command {
    super.runMain("Bike3")
  }

  def run3Final() = T.command {
    super.runMain("Bike3Final")
  }

  override def moduleDeps: Seq[JavaModule] = Seq(constants)
}

object naive extends ScalaModule with MyScalaModule with ScalafmtModule {

  override def moduleDeps: Seq[JavaModule] = Seq(constants)

  def run1() = T.command {
    super.runMain("Bike1")
  }

  def run2() = T.command {
    super.runMain("Bike2")
  }

}

object constants extends ScalaModule with MyScalaModule with ScalafmtModule