package net.jeremycasey.homemonitor.widgets.doorbell

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.R
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme


class DoorbellWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return DoorbellWidgetViewModel(_context) as T
  }
}

class DoorbellWidgetViewModel(context: Context) : ViewModel() {
  private val _context = context

  private var _notificationReceiver: BroadcastReceiver? = null

  private val _latestEvent = MutableLiveData<DoorbellEvent?>(null)
  val latestEvent: LiveData<DoorbellEvent?> = _latestEvent

  fun onDoorbellListenerStart() {
    _notificationReceiver = NotificationReceiver(_latestEvent)
    val filter = IntentFilter()
    filter.addAction("net.jeremycasey.homemonitor.DOORBELL_NOTIFICATION_LISTENER")
    _context.registerReceiver(_notificationReceiver, filter)
  }

  fun onDoorbellListenerEnd() {
    if (_notificationReceiver != null) {
      _context.unregisterReceiver(_notificationReceiver)
      _notificationReceiver = null
    }
  }

  internal class NotificationReceiver(latestEvent: MutableLiveData<DoorbellEvent?>) : BroadcastReceiver() {
    private val _latestEvent = latestEvent

    override fun onReceive(context: Context, intent: Intent) {
      val title = intent.getStringExtra("title")
      val eventType = intent.getStringExtra("eventType")
      val picture = intent.getParcelableExtra("picture") as Bitmap?

      _latestEvent.value = DoorbellEvent(
        title = title as String,
        eventType = eventType as String,
        picture = picture as Bitmap,
      )
    }
  }
}

@Composable
fun DoorbellWidget(viewModel: DoorbellWidgetViewModel) {
  val latestEvent by viewModel.latestEvent.observeAsState()

  DisposableEffect("") {
    viewModel.onDoorbellListenerStart()

    onDispose {
      viewModel.onDoorbellListenerEnd()
    }
  }

  DoorbellWidgetView(latestEvent)
}

@Composable
fun DoorbellWidgetView(latestEvent: DoorbellEvent?) {
  WidgetCard {
    Text("Doorbell")
    if (latestEvent != null) {
      Column {
        Text(latestEvent.title)
        Image(
          painter = BitmapPainter(latestEvent.picture.asImageBitmap()),
          contentDescription = null
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    DoorbellWidgetView(DoorbellEvent(
      title = "There is motion at your Front Door",
      eventType = "motion",
      picture = BitmapFactory.decodeResource(LocalContext.current.getResources(), R.drawable.hello_world)
    ))
  }
}

@Preview(showBackground = true)
@Composable
fun EmptyPreview() {
  HomeMonitorTheme {
    DoorbellWidgetView(null)
  }
}