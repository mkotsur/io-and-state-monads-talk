import BikeConstants._
import cats.data.State
import cats.effect.{ExitCode, IO, IOApp, SyncIO}
import cats.implicits._

import scala.io.StdIn
import scala.language.higherKinds

// Cleaning stuff up with the State monad!

object Bike3Final extends IOApp {

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
        val healthDecrease =
          if (linkPitch(chainCycles) < criticalLinkPitch)
            normalCogDegradation
          else extraCogDegradation
        if (cogHealth > healthDecrease)
          (n.copy(cogHealth = cogHealth - healthDecrease), 42)
        else
          (Condition.Broken("The chain went off"), 0)
    }

    val chain: State[Condition, Int] = for {
      _ <- State[Condition, Unit] {
        case n: Condition.Normal => (n.copy(chainCycles = n.chainCycles + 1), 42)
        case b => (b, 0)
      }
      c1 <- cassette
      c2 <- cassette
      c3 <- cassette
    } yield c1 + c2 + c3

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
