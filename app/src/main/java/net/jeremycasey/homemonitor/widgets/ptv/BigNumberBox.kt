package net.jeremycasey.homemonitor.widgets.ptv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.getReadableTextColor

@Composable
fun BigNumberBox(bgColor: Color, number: String, subText: String? = null) {
  val textColor = getReadableTextColor(bgColor)
  Column (
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly,
    modifier = Modifier
      .width(40.dp)
      .height(40.dp)
      .background(bgColor)
  ) {
    Text(
      text = number,
      style = TextStyle(
        textAlign = TextAlign.Center,
        color = textColor,
        fontSize = if (number.length <= 4) { 20.sp } else { 14.sp },
      ),
      // lineHeight doesn't do anything...why?
//      modifier = Modifier.offset(y = -1.dp)
    )
    if (subText != null) {
      Text(
        subText,
        style = TextStyle(color = textColor, fontSize = 6.sp),
        modifier = Modifier.offset(y = -3.dp)
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun StandardPreview() {
  HomeMonitorTheme {
    BigNumberBox(Color.Blue, "20")
  }
}

@Preview(showBackground = true)
@Composable
fun LightPreview() {
  HomeMonitorTheme {
    BigNumberBox(Color.Yellow, "20")
  }
}

@Preview(showBackground = true)
@Composable
fun ScheduledPreview() {
  HomeMonitorTheme {
    BigNumberBox(Color.Black, "20", "Scheduled")
  }
}

@Preview(showBackground = true)
@Composable
fun LessThan1Preview() {
  HomeMonitorTheme {
    BigNumberBox(Color.Black, "<1")
  }
}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
  HomeMonitorTheme {
    BigNumberBox(Color.Black, "23:59")
  }
}
