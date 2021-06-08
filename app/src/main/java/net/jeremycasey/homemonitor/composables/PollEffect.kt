package net.jeremycasey.homemonitor.composables

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*
import net.jeremycasey.homemonitor.utils.minutesToMs

@Composable
@NonRestartableComposable
fun PollEffect(
  key: Any?,
  intervalMs: Long = minutesToMs(1),
  isEnabled: Boolean = true,
  onInterval: () -> Unit
) {
  if (!isEnabled) return

  DisposableEffect(key, intervalMs) {
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