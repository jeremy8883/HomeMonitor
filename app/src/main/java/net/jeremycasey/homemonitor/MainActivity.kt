package net.jeremycasey.homemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.hideSystemUi
import net.jeremycasey.homemonitor.widgets.calendar.CalendarWidget
import net.jeremycasey.homemonitor.widgets.calendar.CalendarWidgetViewModel
import net.jeremycasey.homemonitor.widgets.calendar.CalendarWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.doorbell.DoorbellWidget
import net.jeremycasey.homemonitor.widgets.doorbell.DoorbellWidgetViewModel
import net.jeremycasey.homemonitor.widgets.doorbell.DoorbellWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.messageBoard.MessageBoardWidget
import net.jeremycasey.homemonitor.widgets.petLog.PetLogWidget
import net.jeremycasey.homemonitor.widgets.petLog.PetLogWidgetViewModel
import net.jeremycasey.homemonitor.widgets.petLog.PetLogWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.ptv.PtvWidget
import net.jeremycasey.homemonitor.widgets.ptv.PtvWidgetViewModel
import net.jeremycasey.homemonitor.widgets.ptv.PtvWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.weather.WeatherWidget
import net.jeremycasey.homemonitor.widgets.weather.WeatherWidgetViewModel
import net.jeremycasey.homemonitor.widgets.weather.WeatherWidgetViewModelFactory
import android.widget.Toast

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import net.jeremycasey.homemonitor.composables.BitmapTransition
import net.jeremycasey.homemonitor.utils.generateBackgroundImage
import net.jeremycasey.homemonitor.widgets.clock.ClockWidget
import net.jeremycasey.homemonitor.widgets.lights.Light
import net.jeremycasey.homemonitor.widgets.lights.LightsWidget
import net.jeremycasey.homemonitor.widgets.lights.LightsWidgetViewModel
import net.jeremycasey.homemonitor.widgets.lights.LightsWidgetViewModelFactory
import net.jeremycasey.homemonitor.widgets.messageBoard.MessageBoardWidgetViewModel
import net.jeremycasey.homemonitor.widgets.messageBoard.MessageBoardWidgetViewModelFactory


private val rootPadding = 10
private val itemPadding = 10

@Composable
private fun WidgetWrapper(
  horizontalF: Float = 1f,
  verticalF: Float = 1f,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = Modifier
      .padding(itemPadding.dp)
      .fillMaxWidth(horizontalF)
      .fillMaxHeight(verticalF)
  ) {
    content()
  }
}

class MainActivity : ComponentActivity() {
  lateinit var _messageBoardViewModel: MessageBoardWidgetViewModel

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
    val messageBoardViewModel by viewModels<MessageBoardWidgetViewModel>() {
      MessageBoardWidgetViewModelFactory(this)
    }
    _messageBoardViewModel = messageBoardViewModel
    val lightsViewModel by viewModels<LightsWidgetViewModel>() {
      LightsWidgetViewModelFactory(this)
    }

    broadcastIntentToViewModelsIfNeeded(this.intent)

    setContent {
      HomeMonitorTheme {
        var background by remember { mutableStateOf<Bitmap>(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)) }

        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          BitmapTransition(
            bitmap = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f),
            contentScale = ContentScale.FillBounds
          )
          Column (modifier = Modifier.padding(rootPadding.dp)) {
            Row(Modifier.fillMaxHeight(0.5f)) {
              Column(
                Modifier
                  .fillMaxWidth(0.66f)
                  .fillMaxHeight()) {
                Row(
                  Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)) {
                  WidgetWrapper(0.5f, 1f) {
                    WeatherWidget(weatherWidgetViewModel)
                  }
                  WidgetWrapper(1f, 1f) {
                    MessageBoardWidget(messageBoardViewModel)
                  }
                }
                WidgetWrapper(1f, 1f) {
                  PetLogWidget(petLogViewModel)
                }
              }
              WidgetWrapper(1f) {
                PtvWidget(ptvViewModel)
              }
            }
            Row (Modifier.fillMaxHeight(1f)) {
              WidgetWrapper(0.34f) {
                CalendarWidget(calendarViewModel)
              }
              WidgetWrapper(0.5f) {
                DoorbellWidget(doorbellViewModel)
              }
              Column(Modifier.fillMaxSize(1f)) {
                WidgetWrapper(1f, 0.5f) {
                  LightsWidget(
                    lightsViewModel,
                    { background = generateBackgroundImage(this@MainActivity, it) }
                  )
                }
                WidgetWrapper(1f, 1f) {
                  ClockWidget()
                }
              }
            }
          }
        }
      }
    }
  }

  private fun broadcastIntentToViewModelsIfNeeded(intent: Intent?) {
    if (intent != null) {
      _messageBoardViewModel.onIntentReceived(intent)
    }
  }

  // Code taken from: https://developer.android.com/training/system-ui/immersive
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) hideSystemUi(window)
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    broadcastIntentToViewModelsIfNeeded(intent)
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
