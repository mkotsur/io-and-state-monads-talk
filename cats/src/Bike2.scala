import Bike2.Condition.{Broken, Normal}
import BikeConstants._
import cats.data.State
import cats.effect.{ExitCode, IO, IOApp, SyncIO}
import cats.implicits._

import scala.io.StdIn
import scala.language.higherKinds

// Cleaning stuff up with the State monad!

object Bike2 extends IOApp {

  sealed trait Condition

  object Condition {
    type Change = Condition => Condition

    case class Normal(cogHealth: Double, chainCycles: Int) extends Condition
    case class Broken(reason: String)                      extends Condition

    def mint = Normal(100, 0)
  }

  val adjustCogHealth: Condition.Change = {
    case wear: Normal =>
      val healthDecrease =
        if (linkPitch(wear.chainCycles) < criticalLinkPitch)
          normalCogDegradation
        else extraCogDegradation
      if (wear.cogHealth > healthDecrease)
        wear.copy(cogHealth = wear.cogHealth - healthDecrease)
      else
        Condition.Broken("The chain went off")
    case b => b
  }

  val mileageIncrease: Condition.Change = {
    case w: Normal => w.copy(chainCycles = w.chainCycles + 1)
    case b         => b
  }

  val cassette: State[Condition, Int] = State({
    case b: Broken => (b, 0)
    case w: Normal => (adjustCogHealth(w), 42)
  })

  val chain: State[Condition, Int] = for {
    c1 <- cassette
    c2 <- cassette
    c3 <- cassette
    _  <- State[Condition, Unit](w => (mileageIncrease(w), ()))
  } yield c1 + c2 + c3

  def run(args: List[String]): IO[ExitCode] = {

    def cycleRun(state: Condition): IO[(Condition, Int)] =
      for {
        (condition, power) <- SyncIO.eval(chain.run(state)).toIO
        _ <- condition match {
          case c @ Condition.Normal(cogHealth, chainCycles) =>
            IO(
              println(
                s"Chain cycles: $chainCycles, Cog health: $cogHealth % , Power: $power"
              )
            ) >> cycleRun(c)
          case Condition.Broken(reason) => IO(println(s"Broken: $reason"))
        }
      } yield (condition, power)

    for {
      _ <- IO(
        println(
          "Hello to our bicycle simulation program. Press ENTER to start!"
        )
      )
      _ <- IO(StdIn.readLine())
      _ <- cycleRun(Condition.mint)
    } yield ExitCode.Success
  }
}
