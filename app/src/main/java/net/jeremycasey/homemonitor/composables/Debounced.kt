package net.jeremycasey.homemonitor.composables

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*

@Composable
fun Debounced(
  delayMs: Long,
  fn: () -> Unit
): () -> Unit {
  var processIdToCall by remember { mutableStateOf<Long>(0) }

  return {
    processIdToCall += 1
    val processId = processIdToCall

    val handler = Handler(Looper.getMainLooper())
    val runnable: Runnable = object : Runnable {
      override fun run() {
        if (processId == processIdToCall) {
          fn()
        }
      }
    }

    handler.postDelayed(runnable, delayMs)
  }
}
