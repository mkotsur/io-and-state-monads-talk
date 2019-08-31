import BikeConstants._

object Bicycle2 extends App {

  var chainCycles  = 1
  var cogHealth    = 100.0 // %

  type Cycle = () => Int
  type AdjustCogHealth = () => Unit

  val adjustCogHealth = () => {
    if (linkPitch(chainCycles) < criticalLinkPitch) {
      // Normal wear while link pitch is below 12mm
      cogHealth -= normalCogDegradation
    } else {
      // Extra quick wear when it's 12mm or more
      cogHealth -= extraCogDegradation
    }

    if (cogHealth <= 0) {
      throw new Error("The chain went off")
    }
  }

  val chainCyclesIncrease = () => chainCycles += 1

  val cassette = (adjustCogHealth: AdjustCogHealth) => {
    adjustCogHealth()
    42 // Units of power 💪
  }

  val chain = (cassetteCycle: () => Int, mileageChange: () => Unit) =>
    () => {
      mileageChange()
      // Fill has a protection against statefulness
      List(cassetteCycle(), cassetteCycle(), cassetteCycle()).sum
    }

  val chainCycle = chain(
    () => cassette(adjustCogHealth),
    chainCyclesIncrease
  )

  var power = 0
  while (true) {
    power += chainCycle()
    println(s"Chain cycles: $chainCycles, Cog health: $cogHealth % , Power: $power")
  }

}
