import Bike3Final.Condition
import cats.data.State

import scala.language.higherKinds
import BikeConstants._
import cats.effect.{ExitCode, IO, IOApp, SyncIO}
import cats.implicits._

import scala.io.StdIn

// Cleaning stuff up with the State monad!

object Bike3 extends IOApp {

  sealed trait Condition

  object Condition {
    case class Normal(cogHealth: Double, chainCycles: Int) extends Condition
    case class Broken(reason: String)                      extends Condition

    def mint = Normal(100, 0)
  }

  def run(args: List[String]): IO[ExitCode] = {

    val cassette: State[Condition, Int] = State {
      case b: Condition.Broken => (b, 0)
      case n @ Condition.Normal(cogHealth, chainCycles) =>
        // TODO: handle health decrease
        // TODO: handle failure
        ???
    }

    val chain: State[Condition, Int] = State { s =>
      // TODO: increase chainCycles
      // TODO: return a sum of 3 cassette cycles
      ???
    }

    def cycleRun(state: Condition, power: Int): IO[(Condition, Int)] =
      for {
        (condition, addedPower) <- ???.asInstanceOf[IO[(Condition, Int)]] // TODO: Run one cycle and convert Eval to IO
        result <- condition match {
          case c @ Condition.Normal(cogHealth, chainCycles) =>
            IO(
              println(
                s"Chain cycles: $chainCycles, Cog health: $cogHealth % , Power: $power"

              )) >> cycleRun(c, power + addedPower)
          case Condition.Broken(_) => IO((condition, power))
        }
      } yield result

    for {
      _ <- IO(
        println(
          "Hello to our bicycle simulation program. Press ENTER to start!"
        )
      )
      _ <- IO(StdIn.readLine())
      _ <- cycleRun(Condition.mint, 0)
      (finalCondition, finalPower) <- cycleRun(Condition.mint, 0)
      _ <- IO(println(s"Final condition: $finalCondition"))
      _ <- IO(println(s"Final power: $finalPower"))
    } yield ExitCode.Success
  }
}
