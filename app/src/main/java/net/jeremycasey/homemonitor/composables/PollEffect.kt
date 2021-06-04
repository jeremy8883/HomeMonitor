package net.jeremycasey.homemonitor.composables

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*

@Composable
@NonRestartableComposable
fun PollEffect(
  key: String,
  intervalMs: Long = 60_000,
  onInterval: () -> Unit
) {
  DisposableEffect(key) {
    val handler = Handler(Looper.getMainLooper())
    val runnable: Runnable = object : Runnable {
      override fun run() {
        onInterval()
        handler.postDelayed(this, intervalMs)
      }
    }

    onInterval()
    handler.postDelayed(runnable, intervalMs)

    onDispose {
      handler.removeCallbacks(runnable)
    }
  }
}