package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WidgetCard(content: @Composable ColumnScope.() -> Unit) {
  Card(elevation = 2.dp) {
    Column(modifier = Modifier.padding(16.dp), content = content)
  }
}