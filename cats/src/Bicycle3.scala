import Bicycle3.Condition.{Broken, Normal}
import cats.data.State

import scala.language.higherKinds
import BikeConstants._
import cats.effect.{ExitCode, IO}

// Cleaning stuff up with the State monad!

object Bicycle3 extends App {

  sealed trait Condition

  object Condition {
    type Change = Condition => Condition

    case class Normal(cogHealth: Double, chainCycles: Int) extends Condition
    case class Broken(reason: String, cogHealth: Double, chainCycles: Int) extends Condition

    def mint = Normal(100, 0)
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

    val adjustCogHealth: Condition.Change = {
      case wear: Normal =>
        val healthDecrease = if (linkPitch(wear.chainCycles) < criticalLinkPitch) normalCogDegradation else extraCogDegradation
        if (wear.cogHealth > healthDecrease)
          wear.copy(cogHealth = wear.cogHealth - healthDecrease)
        else
          Condition.Broken("The chain went off", wear.cogHealth, wear.chainCycles)
      case b => b
    }

    val mileageIncrease: Condition.Change = {
      case w: Normal => w.copy(chainCycles = w.chainCycles + 1)
      case b => b
    }

  var currentState: Condition      = Condition.mint

  while (true) {
    chain.run(currentState).value match {
      case (Broken(reason, _, _), _) =>
        System.err.println(reason)
        System.exit(1)
      case (newState @ Normal(_, _), power) =>
        currentState = newState
        println(s"Chain cycles: ${newState.chainCycles}, Cog health: ${newState.cogHealth} % , Power: $power")
    }
  }
}
