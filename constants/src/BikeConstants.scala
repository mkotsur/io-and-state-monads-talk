object BikeConstants {
  val factoryLinkPitch = 10 // milimeters
  val normalCogDegradation = 0.001 // %
  val criticalLinkPitch = 12
  val extraCogDegradation = 1 // %
  val cassetteCyclePower = 42 // Units of power 💪

  def linkPitch(chainCycles: Int): Double = factoryLinkPitch + chainCycles * 0.001
}