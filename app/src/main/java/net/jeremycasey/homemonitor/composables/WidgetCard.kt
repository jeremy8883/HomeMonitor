package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun WidgetCard(onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
  // TODO WHY WON'T THIS WORK!?
  var modifier = Modifier.height(IntrinsicSize.Max).width(IntrinsicSize.Max)
  if (onClick != null ) {
    modifier = modifier.clickable(
      onClick = onClick,
      role = Role.Button,
    )
  }
  Card(
    elevation = 2.dp,
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(16.dp), content = content)
  }
}
