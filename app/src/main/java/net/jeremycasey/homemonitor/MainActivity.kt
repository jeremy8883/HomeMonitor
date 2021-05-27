package net.jeremycasey.homemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HomeMonitorTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          WeatherWidget()
        }
      }
    }
  }
}

@Composable
fun WeatherWidget() {
  WeatherWidgetView(current = 19.5, min = 10, max = 20)
}

@Composable
fun WeatherWidgetView(current: Number, min: Number, max: Number) {
  Column (modifier = Modifier.padding(16.dp)) {
    Row {
      Text(text = "Current: ")
      Text(text = "$current")
    }
    Row {
      Text(text = "Min: ")
      Text(text = "$min")
    }
    Row {
      Text(text = "Max: ")
      Text(text = "$max")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    WeatherWidgetView(current = 19.5, min = 10, max = 20)
  }
}