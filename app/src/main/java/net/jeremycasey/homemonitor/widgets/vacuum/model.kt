data class VacuumCommand (
  var command: String,
  var initiator: String,
  var time: Long
)

data class VacuumState (
  var batPct: Int,
  var lastCommand: VacuumCommand?,
)
