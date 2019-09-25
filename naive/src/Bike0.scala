import BikeConstants._

object Bike0 extends App {

  var chainCycles  = 1
  var cogHealth    = 100.0 // %

  type Cycle = () => Int

  val cassette: Cycle = () => {
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

    val chain = () => {
    chainCycles += 1

    List(
      cassette(),
      cassette(),
      cassette()
    ).sum
  }

  var power = 0
  while (true) {
    power += chain()
    println(s"Chain cycles: $chainCycles, Cog health: $cogHealth % , Power: $power")
  }

}
