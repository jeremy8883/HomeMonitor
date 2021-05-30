package net.jeremycasey.homemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.jeremycasey.homemonitor.api.bom.WeatherWidget
import net.jeremycasey.homemonitor.api.bom.WeatherWidgetViewModel
import net.jeremycasey.homemonitor.api.bom.WeatherWidgetViewModelFactory
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val weatherWidgetViewModel by viewModels<WeatherWidgetViewModel>() {
      WeatherWidgetViewModelFactory(this)
    }

    setContent {
      HomeMonitorTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Row (modifier = Modifier.padding(20.dp)) {
            WeatherWidget(weatherWidgetViewModel)
          }
        }
      }
    }
  }
}
