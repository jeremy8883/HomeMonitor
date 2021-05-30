package net.jeremycasey.homemonitor.api.bom

import Clouds
import Coord
import CurrentWeather
import Main
import Sys
import Weather
import Wind
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fetchCurrentWeather
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme

val mockCurrentWeather = CurrentWeather(
  coord = Coord( lon = 144.9633, lat = -37.814 ),
  weather = listOf(
    Weather( id = 801, main = "Clouds", description = "few clouds", icon = "02d" )
  ),
  base = "stations",
  main = Main(
    temp = 20.33,
    feelsLike = 19.31,
    tempMin = 15.76,
    tempMax = 25.06,
    pressure = 1031,
    humidity = 69
  ),
  visibility = 10000,
  wind = Wind( speed = 0.89, deg = 328, gust = 1.34 ),
  clouds = Clouds( all = 20 ),
  dt = 1622338809,
  sys = Sys(
    type = 2,
    id = 2008797,
    country = "AU",
    sunrise = 1622323511,
    sunset = 1622358627
  ),
  timezone = 36000,
  id = 2158177,
  name = "Melbourne",
  cod = 200
)

class WeatherWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return WeatherWidgetViewModel(_context) as T
  }
}

class WeatherWidgetViewModel(context: Context) : ViewModel() {
  private val _currentWeather = MutableLiveData<CurrentWeather?>(null)
  val currentWeather: LiveData<CurrentWeather?> = _currentWeather

  private val _context = context

  private val _fetchError = MutableLiveData<Exception?>(null)
  val fetchError: LiveData<Exception?> = _fetchError

  fun onWeatherRequired() {
    fetchCurrentWeather(
      _context,
      { currentWeather: CurrentWeather ->
        _currentWeather.value = currentWeather
      },
      { error: Exception ->
        _fetchError.value = error
      }
    )
  }
}

@Composable
fun WeatherWidget(viewModel: WeatherWidgetViewModel) {
  val currentWeather by viewModel.currentWeather.observeAsState()
  val fetchError by viewModel.fetchError.observeAsState()

  LaunchedEffect("") {
    viewModel.onWeatherRequired()
  }

  WeatherWidgetView(currentWeather, fetchError, { viewModel.onWeatherRequired() })
}

@Composable
fun WeatherWidgetView(currentWeather: CurrentWeather?, fetchError: Exception?, onRetryClick: () -> Any) {
  if (fetchError != null) {
    Column (modifier = Modifier.padding(16.dp)) {
      Text("Error: ${fetchError.message}")
      Button(onClick = {
        println("clicked??")
        onRetryClick()
      }) {
        Text("Retry")
      }

    }
    return
  }
  if (currentWeather == null) {
    Column (modifier = Modifier.padding(16.dp)) {
      Text("Loading...")
    }
    return
  }

  Column (modifier = Modifier.padding(16.dp)) {
    Row {
      Text(text = "Current: ")
      Text(text = "${currentWeather.main.temp}")
    }
    Row {
      Text(text = "Feels like ${currentWeather.main.feelsLike}")
    }
    Row {
      Text(text = "Min: ")
      Text(text = "${currentWeather.main.tempMin}")
    }
    Row {
      Text(text = "Max: ")
      Text(text = "${currentWeather.main.tempMax}")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    WeatherWidgetView(mockCurrentWeather, null, { })
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    WeatherWidgetView(null, null, { })
  }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
  HomeMonitorTheme {
    WeatherWidgetView(null, Exception("This is an error"), { })
  }
}
