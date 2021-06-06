package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> DividedList(list: List<T>, Divider: @Composable () -> Unit, modifier: Modifier = Modifier, content: @Composable (item: T) -> Unit) {
  Column(modifier) {
    list.forEachIndexed { index, item ->
      if (index >= 1) {
        Divider()
      }
      content(item)
    }
  }
}