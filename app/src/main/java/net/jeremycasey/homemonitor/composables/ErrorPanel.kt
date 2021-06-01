package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun ErrorPanel(fetchError: Exception, onRetryClick: () -> Any) {
  Text(
    text = "Error: ${fetchError.message}",
    style = TextStyle(
      color = MaterialTheme.colors.error
    ),
    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
  )
  Button(onClick = {
    onRetryClick()
  }) {
    Text("Retry")
  }
}
