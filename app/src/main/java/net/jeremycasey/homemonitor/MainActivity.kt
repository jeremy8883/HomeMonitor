package net.jeremycasey.homemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.Size
import net.jeremycasey.homemonitor.utils.getScreenSize
import net.jeremycasey.homemonitor.utils.hideSystemUi
import net.jeremycasey.homemonitor.widgets.calendar.CalendarWidget
import net.jeremycasey.homemonitor.widgets.calendar.CalendarWidgetViewModel
import net.jeremycasey.homemonitor.widgets.calendar.CalendarWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.doorbell.DoorbellWidget
import net.jeremycasey.homemonitor.widgets.doorbell.DoorbellWidgetViewModel
import net.jeremycasey.homemonitor.widgets.doorbell.DoorbellWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.petLog.PetLogWidget
import net.jeremycasey.homemonitor.widgets.petLog.PetLogWidgetViewModel
import net.jeremycasey.homemonitor.widgets.petLog.PetLogWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.ptv.PtvWidget
import net.jeremycasey.homemonitor.widgets.ptv.PtvWidgetViewModel
import net.jeremycasey.homemonitor.widgets.ptv.PtvWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.weather.WeatherWidget
import net.jeremycasey.homemonitor.widgets.weather.WeatherWidgetViewModel
import net.jeremycasey.homemonitor.widgets.weather.WeatherWidgetViewModelFactory

@Composable
private fun WidgetWrapper(percent: Double, screenSize: Size, content: @Composable BoxScope.() -> Unit) {
  Box(
    modifier = Modifier
      .padding(20.dp)
      .height(IntrinsicSize.Max)
      .width(((screenSize.width - 40) * (percent / 100)).dp)
  ) {
    content()
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val weatherWidgetViewModel by viewModels<WeatherWidgetViewModel>() {
      WeatherWidgetViewModelFactory(this)
    }
    val petLogViewModel by viewModels<PetLogWidgetViewModel>() {
      PetLogWidgetViewModelFactory(this)
    }
    val ptvViewModel by viewModels<PtvWidgetViewModel>() {
      PtvWidgetViewModelFactory(this)
    }
    val doorbellViewModel by viewModels<DoorbellWidgetViewModel>() {
      DoorbellWidgetViewModelFactory(this)
    }
    val calendarViewModel by viewModels<CalendarWidgetViewModel>() {
      CalendarWidgetViewModelFactory(this)
    }
    val screenSize = getScreenSize(this)

    setContent {
      HomeMonitorTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Column (modifier = Modifier.padding(20.dp)) {
            Row {
              WidgetWrapper(50.0, screenSize) {
                PetLogWidget(petLogViewModel)
              }
              WidgetWrapper(25.0, screenSize) {
                WeatherWidget(weatherWidgetViewModel)
              }
              WidgetWrapper(25.0, screenSize) {
                PtvWidget(ptvViewModel)
              }
            }
            Row {
              WidgetWrapper(34.0, screenSize) {
                CalendarWidget(calendarViewModel)
              }
              WidgetWrapper(66.0, screenSize) {
                DoorbellWidget(doorbellViewModel)
              }
            }
          }
        }
      }
    }
  }

  // Code taken from: https://developer.android.com/training/system-ui/immersive
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) hideSystemUi(window)
  }

//  fun timedOpenAppBackUp() {
//    val futureDate = DateTime.now().plusMillis(appCloseTimeoutMs)
//
//    val al = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    val intent = Intent(this, AlarmReceiver::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
//    al.set(AlarmManager.RTC_WAKEUP, futureDate.millis, pendingIntent)
//
//    val toast = Toast.makeText(this, "aaa", 3 * 1000)
//    toast.show()
//  }
//
//  override fun onPause() {
//    super.onPause()
//    timedOpenAppBackUp()
//  }
}

//class AlarmReceiver : BroadcastReceiver() {
//  override fun onReceive(context: Context, intent: Intent?) {
//    val toast = Toast.makeText(context, "hello", 3 * 1000)
//    toast.show()
//
//    val activityIntent = Intent(context, MainActivity::class.java)
//    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    context.startActivity(activityIntent)
//  }
//}
