package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun WidgetCard(onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
  if (onClick != null) {
      Card(elevation = 2.dp, modifier = Modifier.clickable(
        onClick = onClick,
        role = Role.Button,
      )) {
        Column(modifier = Modifier.padding(16.dp), content = content)
      }
  } else {
    Card(elevation = 2.dp) {
      Column(modifier = Modifier.padding(16.dp), content = content)
    }
  }
}