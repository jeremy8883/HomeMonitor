package net.jeremycasey.homemonitor.widgets.weather.api

import GsonRequest
import android.content.Context
import com.android.volley.Response
import net.jeremycasey.homemonitor.private.apiKey
import net.jeremycasey.homemonitor.private.cityId
import net.jeremycasey.homemonitor.private.units
import net.jeremycasey.homemonitor.widgets.weather.CurrentWeather

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
