package net.jeremycasey.homemonitor

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
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

    setContent {
      HomeMonitorTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Row (modifier = Modifier.padding(20.dp)) {
            WeatherWidget(weatherWidgetViewModel)
            PetLogWidget(petLogViewModel)
            PtvWidget(ptvViewModel)
            DoorbellWidget(doorbellViewModel)
          }
        }
      }
    }
  }

  // Code taken from: https://developer.android.com/training/system-ui/immersive
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) hideSystemUI()
  }

  private fun hideSystemUI() {
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
        // Set the content to appear under the system bars so that the
        // content doesn't resize when the system bars hide and show.
        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // Hide the nav bar and status bar
        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_FULLSCREEN)
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
