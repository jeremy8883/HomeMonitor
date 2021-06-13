package net.jeremycasey.homemonitor.widgets.clock

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.composables.WithCurrentTime
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import org.joda.time.DateTime

@Composable
fun ClockWidget() {
  WithCurrentTime(recomposeInterval = 1000) { now ->
    ClockWidgetView(now)
  }
}

@Composable
fun ClockWidgetView(now: DateTime) {
  WidgetCard(padding = 0.dp) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxSize(1f)
    ) {
      Text(
        text = now.toString("HH:mm"),
        style = TextStyle(
          lineHeight = 0.sp,
          fontSize = 80.sp,
          fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
          .padding(10.dp, 0.dp, 20.dp, 0.dp)
      )
      Text(
        text = now.toString("EEE, dd MMM").replace(".", ""),
        style = TextStyle(
          color = Color(0xFF878787),
          fontSize = 34.sp,
        ),
        modifier = Modifier.offset(y = -10.dp).padding(0.dp, 0.dp, 0.dp, 10.dp)
      )
    }
  }
}

@Preview(showBackground = true, widthDp = 350, heightDp = 200)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    ClockWidgetView(DateTime.now())
  }
}
