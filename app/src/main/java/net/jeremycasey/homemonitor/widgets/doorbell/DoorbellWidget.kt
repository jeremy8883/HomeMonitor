package net.jeremycasey.homemonitor.widgets.doorbell

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import net.jeremycasey.homemonitor.composables.WithCurrentTime
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.openApp
import net.jeremycasey.homemonitor.utils.toRelativeDateString
import org.joda.time.DateTime

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
  private var _contentIntent = MutableLiveData<PendingIntent?>(null)

  private val _latestEvent = MutableLiveData<DoorbellEvent?>(null)
  val latestEvent: LiveData<DoorbellEvent?> = _latestEvent

  fun onDoorbellListenerStart() {
    _notificationReceiver = NotificationReceiver(_latestEvent, _contentIntent)
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

  fun onCardTouch() {
    if (_contentIntent.value != null) {
      _contentIntent.value!!.send()
    } else {
      openApp(_context, "com.ring.answer")
    }
  }

  internal class NotificationReceiver(latestEvent: MutableLiveData<DoorbellEvent?>, contentIntent: MutableLiveData<PendingIntent?>) : BroadcastReceiver() {
    private val _latestEvent = latestEvent
    private val _contentIntent = contentIntent

    override fun onReceive(context: Context, intent: Intent) {
      val title = intent.getStringExtra("title")
      val eventType = intent.getStringExtra("eventType")
      val picture = intent.getParcelableExtra("picture") as Bitmap?
      val contentIntent = intent.getParcelableExtra("contentIntent") as PendingIntent?
      val dateTime = intent.getStringExtra("dateTime")

      _contentIntent.value = contentIntent
      _latestEvent.value = DoorbellEvent(
        title = title as String,
        eventType = eventType as String,
        picture = picture as Bitmap,
        dateTime = DateTime(dateTime as String),
      )
    }
  }
}

@Composable
fun DoorbellWidget(viewModel: DoorbellWidgetViewModel) {
  val latestEvent by viewModel.latestEvent.observeAsState()

  DisposableEffect(Unit) {
    viewModel.onDoorbellListenerStart()

    onDispose {
      viewModel.onDoorbellListenerEnd()
    }
  }

  WithCurrentTime { now ->
    DoorbellWidgetView(latestEvent, now, { viewModel.onCardTouch() })
  }
}

@Composable
fun DoorbellWidgetView(latestEvent: DoorbellEvent?, now: DateTime, onCardTouch: () -> Unit) {
  WidgetCard(onCardTouch) {
    Text("Doorbell", style = MaterialTheme.typography.h4)
    if (latestEvent != null) {
      Column {
        Text(latestEvent.title)
        Text(
          toRelativeDateString(latestEvent.dateTime, now),
          style = MaterialTheme.typography.subtitle1
        )
        Image(
          painter = BitmapPainter(latestEvent.picture.asImageBitmap()),
          contentDescription = null
        )
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 200)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    DoorbellWidgetView(
      DoorbellEvent(
        title = "There is motion at your Front Door",
        eventType = "motion",
        picture = BitmapFactory.decodeResource(LocalContext.current.getResources(), R.drawable.hello_world),
        dateTime = DateTime.now().minusMinutes(2),
      ), DateTime.now(), {}
    )
  }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 200)
@Composable
fun EmptyPreview() {
  HomeMonitorTheme {
    DoorbellWidgetView(null, DateTime.now(), {})
  }
}