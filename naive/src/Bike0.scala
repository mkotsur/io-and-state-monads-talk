import BikeConstants._

object Bike0 extends App {

  var chainCycles  = 1
  var cogHealth    = 100.0 // %

  type Cycle = () => Int

  val cassetteCycle: Cycle = () => {
    if (linkPitch(chainCycles) < criticalLinkPitch) {
      cogHealth -= normalCogDegradation
    } else {
      cogHealth -= extraCogDegradation
    }

    if (cogHealth <= 0) {
      throw new Error("The chain went off")
    }
    cassetteCyclePower
  }

    val chainCycle = () => {
    chainCycles += 1

    List(
      cassetteCycle(),
      cassetteCycle(),
      cassetteCycle()
    ).sum
  }

  var power = 0
  while (true) {
    power += chainCycle()
    println(s"Chain cycles: $chainCycles, Cog health: $cogHealth % , Power: $power")
  }

}
