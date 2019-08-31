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

    val cassette: State[Condition, Int] = State { s => ??? }

    val chain: State[Condition, Int] = State { s => ??? }

    def cycleRun(state: Condition): IO[(Condition, Int)] = {
      // Implement in terms of cassette and chain
      IO((state, 0))
    }

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
