package net.jeremycasey.homemonitor.widgets.tv

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.*
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.secondsToMs

val mockTvState = TvState(
    isOn = true
  )

class TvWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return TvWidgetViewModel(_context) as T
  }
}

class TvWidgetViewModel(
  context: Context,
) : ViewModel() {
  private val _tvState = MutableLiveData<TvState?>(TvState(isOn = false))
  val tvState: LiveData<TvState?> = _tvState

  private val _context = context

  private val _stateFetchError = MutableLiveData<Exception?>(null)
  val stateFetchError: LiveData<Exception?> = _stateFetchError

  fun onTvDataRequired() {
//    _stateFetchError.value = null
//    _tvState.value = TvState(isOn = false)
//    fetchTvState(
//      _context,
//      { lightGroups: Map<String, LightGroup> ->
//        _tvState.value = lightGroups
//      },
//      { error: Exception ->
//        _stateFetchError.value = error
//      }
//    )
  }

  fun onToggleGroup(isOn: Boolean) {
    toggleTv(
      isOn, _context,
      {
        _tvState.value = TvState(isOn = isOn)
      },
      {
        println(it)
      }
    )
  }
}

@Composable
fun TvWidget(viewModel: TvWidgetViewModel) {
  val tvState by viewModel.tvState.observeAsState()
  val fetchError by viewModel.stateFetchError.observeAsState()

  PollEffect(Unit, secondsToMs(10)) {
    viewModel.onTvDataRequired()
  }

  TvWidgetView(
    tvState,
    fetchError,
    { viewModel.onTvDataRequired() },
    { isOn -> viewModel.onToggleGroup(isOn) },
  )
}

@Composable
fun TvWidgetView(
  tvState: TvState?,
  fetchError: Exception?,
  onRetryClick: () -> Any,
  onToggle: (isOn: Boolean) -> Unit
) {
  if (fetchError != null) {
    WidgetCard {
      ErrorPanel(fetchError, onRetryClick)
    }
    return
  }
  if (tvState == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }

  WidgetCard {
    ButtonSwitch(tvState.isOn, { onToggle(it) }, "Tv")
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    TvWidgetView(mockTvState, null, { }, { _ -> })
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    TvWidgetView(null, null, { }, { _ -> })
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun ErrorPreview() {
  HomeMonitorTheme {
    TvWidgetView(null, Exception("This is an error"), { }, { _ -> })
  }
}
