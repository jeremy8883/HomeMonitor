package net.jeremycasey.homemonitor.widgets.shared

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle


@Composable
fun LoadingPanel() {
  Text(
    text = "Loading...",
    style = TextStyle(
      fontStyle = FontStyle.Italic,
      color = Color(0xffbababa)
    )
  )
}
