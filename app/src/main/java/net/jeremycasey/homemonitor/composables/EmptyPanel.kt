package net.jeremycasey.homemonitor.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmptyPanel(text: String) {
  Text(
    text = text,
    style = TextStyle(textAlign = TextAlign.Center, color = Color.Gray, fontStyle = FontStyle.Italic)
  )
}

@Preview
@Composable
fun EmptyPanelPreview() {
  EmptyPanel(text = "There are no messages")
}