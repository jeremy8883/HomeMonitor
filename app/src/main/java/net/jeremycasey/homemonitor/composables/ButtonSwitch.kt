package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class Variant {
  switch,
  checkbox
}

@Composable
fun ButtonSwitch(isChecked: Boolean, onCheckedChange: (isChecked: Boolean) -> Unit, text: String, variant: Variant = Variant.switch) {
  var isCheckedOptimistic by remember { mutableStateOf<Boolean?>(null) }

  val isCheckedToShow: Boolean = if (isCheckedOptimistic != null)
    isCheckedOptimistic as Boolean
  else
    isChecked

  val handleCheckedChanged = {
    isCheckedOptimistic = !isCheckedToShow
    onCheckedChange(!isCheckedToShow)
  }

  Column(
    Modifier
      .width(80.dp)
      .height(80.dp)
      .clickable(onClick = handleCheckedChanged)
      .border(1.dp, Color.Gray),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    if (variant == Variant.checkbox) {
      Checkbox(isChecked, { handleCheckedChanged() })
    } else {
      Switch(isChecked, { handleCheckedChanged() })
    }
    Text(text, style = TextStyle(
      color = Color(0xFF878787),
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
    ), modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp))
  }
}

@Preview
@Composable
fun ButtonSwitchPreviewDefault() {
  ButtonSwitch(isChecked = true, onCheckedChange = { }, text = "Hello world")
}
