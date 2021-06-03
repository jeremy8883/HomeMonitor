package net.jeremycasey.homemonitor.composables

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*
import org.joda.time.DateTime

@Composable
@NonRestartableComposable
fun WithCurrentTime(
  recomposeInterval: Long = 60_000,
  content: @Composable (now: DateTime) -> Unit
) {
  var now by remember { mutableStateOf(DateTime.now()) }

  DisposableEffect("") {
    val handler = Handler(Looper.getMainLooper())
    val runnable: Runnable = object : Runnable {
      override fun run() {
        now = DateTime.now()
        handler.postDelayed(this, recomposeInterval)
      }
    }

    handler.postDelayed(runnable, recomposeInterval)

    onDispose {
      handler.removeCallbacks(runnable)
    }
  }

  content(now)
}