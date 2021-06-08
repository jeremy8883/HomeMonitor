package net.jeremycasey.homemonitor.widgets.weather

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.*
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.round
import net.jeremycasey.homemonitor.utils.hoursToMs
import net.jeremycasey.homemonitor.widgets.weather.api.*

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
    _fetchError.value = null
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

  PollEffect("", hoursToMs(1)) {
    viewModel.onWeatherRequired()
  }

  WeatherWidgetView(currentWeather, fetchError, { viewModel.onWeatherRequired() })
}

private fun getWeatherIcon(weather: Weather): String {
  return "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
}

@Composable
fun WeatherWidgetView(currentWeather: CurrentWeather?, fetchError: Exception?, onRetryClick: () -> Any) {
  if (fetchError != null) {
    WidgetCard {
      ErrorPanel(fetchError, onRetryClick)
    }
    return
  }
  if (currentWeather == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }

  WidgetCard {
    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      RemoteImage(
        src = getWeatherIcon(currentWeather.weather[0]),
        contentDescription = null,
        modifier = Modifier.width(100.dp).height(100.dp)
      )

      Text(
        text = "${round(currentWeather.main.temp, 0).toInt()}°",
        style = MaterialTheme.typography.h1 + TextStyle(
          lineHeight = 0.sp
        ),
        modifier = Modifier.padding(10.dp, 0.dp, 20.dp, 0.dp)
      )
      Column {
        Text(
          text = "Feels like ${round(currentWeather.main.feelsLike, 0).toInt()}°",
          style = TextStyle(
            color = Color(0xFF878787),
            fontSize = 14.sp,
          )
        )
        Row {
          Text(
            text = "${round(currentWeather.main.tempMax, 0).toInt()}°",
            style = TextStyle(
              color = Color(0xff878787),
              fontSize = 18.sp,
            )
          )
          Text(
            text = " ${round(currentWeather.main.tempMin, 0).toInt()}°",
            style = TextStyle(
              color = Color(0xffbababa),
              fontSize = 18.sp,
            )
          )
        }
      }
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
