package net.jeremycasey.homemonitor.widgets.vacuum

import VacuumCommand
import VacuumState
import android.content.Context
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.ErrorPanel
import net.jeremycasey.homemonitor.composables.LoadingPanel
import net.jeremycasey.homemonitor.composables.PollEffect
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.secondsToMs

val stateMock = VacuumState(
  batPct = 89,
  lastCommand = VacuumCommand(
    command = "dock",
    initiator = "rmtApp",
    time = 1625916983,
  ),
)

class VacuumWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return VacuumWidgetViewModel(_context) as T
  }
}

class VacuumWidgetViewModel(
  context: Context,
) : ViewModel() {
  private val _state = MutableLiveData<VacuumState>(null)
  val state: LiveData<VacuumState> = _state
  
  private val _context = context

  private val _stateFetchError = MutableLiveData<Exception?>(null)
  val stateFetchError: LiveData<Exception?> = _stateFetchError

  fun onVacuumDataRequired() {
    _stateFetchError.value = null

//    fetchVacuumstate(
//      _context,
//      { Vacuum: Map<String, Light> ->
//        _allVacuum.value = Vacuum
//      },
//      { error: Exception ->
//        println(error)
//      }
//    )
  }
}

@Composable
fun VacuumWidget(viewModel: VacuumWidgetViewModel) {
  val state by viewModel.state.observeAsState()
  val fetchError by viewModel.stateFetchError.observeAsState()

  PollEffect(Unit, secondsToMs(10)) {
    viewModel.onVacuumDataRequired()
  }

  VacuumWidgetView(
    state,
    fetchError,
    { viewModel.onVacuumDataRequired() }
  )
}

@Composable
fun VacuumWidgetView(state: VacuumState?, fetchError: Exception?, onRetryClick: () -> Unit) {
  if (fetchError != null) {
    WidgetCard {
      ErrorPanel(fetchError, onRetryClick)
    }
    return
  }
  if (state == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }
  WidgetCard {
    Text("${state.batPct}")
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    VacuumWidgetView(stateMock, null, {})
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    VacuumWidgetView(null, null, {})
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun ErrorPreview() {
  HomeMonitorTheme {
    VacuumWidgetView(null, Exception("This is an error"), {})
  }
}
