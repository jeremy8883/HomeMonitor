package net.jeremycasey.homemonitor.api.openWeather

import GsonRequest
import android.content.Context
import com.android.volley.Response
import net.jeremycasey.homemonitor.private.apiKey
import net.jeremycasey.homemonitor.private.cityId
import net.jeremycasey.homemonitor.private.units

/**
Example:
{
  "coord": { "lon": 144.9633, "lat": -37.814 },
  "weather": [
    { "id": 801, "main": "Clouds", "description": "few clouds", "icon": "02d" }
  ],
  "base": "stations",
  "main": {
    "temp": 284.33,
    "feels_like": 283.31,
    "temp_min": 282.76,
    "temp_max": 287.06,
    "pressure": 1031,
    "humidity": 69
  },
  "visibility": 10000,
  "wind": { "speed": 0.89, "deg": 328, "gust": 1.34 },
  "clouds": { "all": 20 },
  "dt": 1622338809,
  "sys": {
    "type": 2,
    "id": 2008797,
    "country": "AU",
    "sunrise": 1622323511,
    "sunset": 1622358627
  },
  "timezone": 36000,
  "id": 2158177,
  "name": "Melbourne",
  "cod": 200
}
 */
data class CurrentWeather (
  val coord: Coord,
  val weather: List<Weather>,
  val base: String,
  val main: Main,
  val visibility: Long,
  val wind: Wind,
  val clouds: Clouds,
  val dt: Long,
  val sys: Sys,
  val timezone: Long,
  val id: Long,
  val name: String,
  val cod: Long
)

data class Clouds (
  val all: Long
)

data class Coord (
  val lon: Double,
  val lat: Double
)

data class Main (
  val temp: Double,
  val feelsLike: Double,
  val tempMin: Double,
  val tempMax: Double,
  val pressure: Long,
  val humidity: Long
)

data class Sys (
  val type: Long,
  val id: Long,
  val country: String,
  val sunrise: Long,
  val sunset: Long
)

data class Weather (
  val id: Long,
  val main: String,
  val description: String,
  val icon: String
)

data class Wind (
  val speed: Double,
  val deg: Long,
  val gust: Double
)

fun fetchCurrentWeather(context: Context, listener: Response.Listener<CurrentWeather>, errorListener: Response.ErrorListener) {
  val request = GsonRequest<CurrentWeather>(
    "https://api.openweathermap.org/data/2.5/weather?id=$cityId&appid=$apiKey&units=$units",
    CurrentWeather::class.java,
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}
