object Bicycle0 extends App {
  val newLinkPitch = 10 // milimeters
  val normalCogDegradation = 0.001 // %
  val criticalLinkPitch = 12
  val extraCogDegradation = 1 // %
  val cassetteCyclePower = 42 // Units of power 💪

  var chainCycles  = 1
  var cogHealth    = 100.0 // %

  def linkPitch = newLinkPitch + chainCycles * 0.001

  type Cycle = () => Int

  val cassetteCycle: Cycle = () => {
    if (linkPitch < criticalLinkPitch) {
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
