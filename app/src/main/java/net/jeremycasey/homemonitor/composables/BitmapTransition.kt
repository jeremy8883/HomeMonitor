package net.jeremycasey.homemonitor.composables

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale

@Composable
fun BitmapTransition(
  bitmap: Bitmap?,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Fit
) {
  var bitmapA by remember { mutableStateOf<Bitmap?>(bitmap) }
  var bitmapB by remember { mutableStateOf<Bitmap?>(null) }
  var isB by remember { mutableStateOf<Boolean>(false) }

  LaunchedEffect(bitmap) {
    if (isB) {
      bitmapA = bitmap
    } else {
      bitmapB = bitmap
    }
    isB = !isB
  }

  Crossfade(targetState = isB, animationSpec = tween(3000)) { screen: Boolean ->
    val bm = when (screen) {
      false -> bitmapA
      true -> bitmapB
    }

    if (bm == null) { Box {} }
    else {
      Image(
        painter = BitmapPainter(bm.asImageBitmap()),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
      )
    }
  }
}