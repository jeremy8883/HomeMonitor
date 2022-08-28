package net.jeremycasey.homemonitor.widgets.tv

data class Tv(
  val name: String,
  val state: TvState,
)

data class TvState(
  val isOn: Boolean,
)
