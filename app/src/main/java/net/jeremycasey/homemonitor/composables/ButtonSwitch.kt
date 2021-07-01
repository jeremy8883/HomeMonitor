package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonSwitch(isChecked: Boolean, onCheckedChange: (isChecked: Boolean) -> Unit, text: String) {
  Column(
    Modifier
      .width(80.dp)
      .height(80.dp)
      .clickable(onClick = { onCheckedChange(!isChecked) })
      .border(1.dp, Color.Gray),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Switch(isChecked, { onCheckedChange(!isChecked) })
    Text(text, style = TextStyle(
      color = Color(0xFF878787),
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
    ), modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp))
  }
}
