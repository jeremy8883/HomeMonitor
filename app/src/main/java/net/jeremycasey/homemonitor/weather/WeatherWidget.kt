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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun WeatherWidget(context: Context) {
  var (currentWeather, setCurrentWeather) = remember { mutableStateOf<CurrentWeather?>(null) }
  var (fetchError, setFetchError) = remember { mutableStateOf<Exception?>(null) }

  fun onWeatherRequired() {
    fetchCurrentWeather(
      context,
      { _currentWeather: CurrentWeather ->
        setCurrentWeather(_currentWeather)
        setFetchError(null)
      },
      { error: Exception ->
        setFetchError(error)
      }
    )
  }

  LaunchedEffect("") {
    onWeatherRequired()
  }

  WeatherWidgetView(currentWeather, fetchError, { onWeatherRequired() })
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
