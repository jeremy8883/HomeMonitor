package net.jeremycasey.homemonitor.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class Scrollable {
  horizontal,
  vertical,
  none,
}

@Composable
fun WidgetCard(onClick: (() -> Unit)? = null, scrollable: Scrollable = Scrollable.none,
               padding: Dp = 16.dp,
               content: @Composable ColumnScope.() -> Unit) {
  // I tried using IntrinsicSize.Max, but it wouldn't work. Now I need to prop drill the width and height of every single widget.
  var modifier = Modifier.fillMaxSize()
  if (onClick != null ) {
    modifier = modifier.clickable(
      onClick = onClick,
      role = Role.Button,
    )
  }

  var innerModifier = Modifier.fillMaxSize()
  if (scrollable == Scrollable.vertical) {
    innerModifier = innerModifier.verticalScroll(rememberScrollState())
  } else if (scrollable == Scrollable.horizontal) {
    innerModifier = innerModifier.horizontalScroll(rememberScrollState())
  }
  Card(
    elevation = 2.dp,
    modifier = modifier
  ) {
    Box(innerModifier) {
      Column(modifier = Modifier.padding(padding), content = content)
    }
  }
}
