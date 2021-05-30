package net.jeremycasey.homemonitor.api.bom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme

val mockWeatherSummary = WeatherSummary(
  apparentTemp = 10.4,
  deltaT = 0.4,
  gustKmh = 15.0,
  windGustSpeed = 8.0,
  airTemperature = 11.5,
  dewPoint = 10.7,
  pres = 1027.7,
  mslPres = 1027.7,
  qnhPres = 1027.7,
  rainHour = 0.4,
  rainTen = 0.0,
  relHumidity = 95.0,
  visKm = 10.0,
  windDir = "WSW",
  windDirDeg = 249.0,
  windSpeedKmh = 7.0,
  windSpeed = 4.0,
)

val mockWeatherDailyForecast = WeatherDailyForecast(
  airTemperatureMinimum = null,
  airTemperatureMaximum = 15.0,
  endTimeLocal = "2021-05-28T00:00:00+10:00",
  endTimeUtc = "2021-05-28T00:00:00+10:00",
  forecastIconCode = "11",
  index = 0,
  startTimeLocal = "2021-05-27T05:00:00+10:00",
  startTimeUtc = "2021-05-26T19:00:00Z",
  precis = "Showers.",
  probabilityOfPrecipitation = "90%"
)

class WeatherWidgetViewModel : ViewModel() {
  private val _weatherSummary = MutableLiveData<WeatherSummary?>(null)
  val weatherSummary: LiveData<WeatherSummary?> = _weatherSummary

  private val _dailyForecast = MutableLiveData<WeatherDailyForecast?>(null)
  val dailyForecast: LiveData<WeatherDailyForecast?> = _dailyForecast

  fun onWeatherRequired() {
    CoroutineScope(IO).launch {
      try {
        val weather = fetchWeatherSummary()
        println("fetched and returned weather")
        setWeatherSummaryOnMainThread(weather)
      } catch (ex: Exception) {
        setErrorOnMainThread(ex)
      }
    }
    // _weatherSummary.value =
    // _dailyForecast.value =
  }

  private suspend fun setWeatherSummaryOnMainThread(weatherSummary: WeatherSummary) {
    withContext(Main) {
      _weatherSummary.value = weatherSummary
    }
  }

  private suspend fun setErrorOnMainThread(error: Exception) {
    withContext(Main) {
      // TODO
      // _error.value = weatherSummary
    }
  }
}

@Composable
fun WeatherWidget(viewModel: WeatherWidgetViewModel) {
  val weatherSummary by viewModel.weatherSummary.observeAsState()
  val dailyForecast by viewModel.dailyForecast.observeAsState()

  LaunchedEffect("") {
    viewModel.onWeatherRequired()
  }

  WeatherWidgetView(weatherSummary, dailyForecast)
}

@Composable
fun WeatherWidgetView(summary: WeatherSummary?, dailyForecast: WeatherDailyForecast?) {
  if (summary == null && dailyForecast == null) {
    Text("Loading...")
    return
  }

  Column (modifier = Modifier.padding(16.dp)) {
    Row {
      Text(text = "Current: ")
      Text(text = "${summary?.airTemperature}")
    }
    Row {
      Text(text = "Feels like ${summary?.apparentTemp}")
    }
    if (dailyForecast?.airTemperatureMinimum != null) {
      Row {
        Text(text = "Min: ")
        Text(text = "${dailyForecast.airTemperatureMinimum}")
      }
    }
    if (dailyForecast?.airTemperatureMaximum != null) {
      Row {
        Text(text = "Max: ")
        Text(text = "${dailyForecast.airTemperatureMaximum}")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    WeatherWidgetView(mockWeatherSummary, mockWeatherDailyForecast)
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    WeatherWidgetView(null, null)
  }
}
