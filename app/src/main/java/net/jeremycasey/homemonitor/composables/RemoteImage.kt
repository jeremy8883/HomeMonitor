package net.jeremycasey.homemonitor.composables

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun RemoteImage(src: String, contentDescription: String?, modifier: Modifier = Modifier) {
  var bitmapPainter by remember { mutableStateOf<BitmapPainter?>(null) }
  var error by remember { mutableStateOf<Exception?>(null) }

  LaunchedEffect(src) {
    Picasso.get().load(src).into(object: Target {
      override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
      }

      override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        error = e
      }

      override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        bitmapPainter = BitmapPainter(bitmap!!.asImageBitmap())
      }
    })
  }

  if (bitmapPainter == null) {
    if (error != null) {
      Box(
        modifier = modifier.background(Color.Red)
      )
    } else {
      Box(
        modifier = modifier
      )
    }
  } else {
    Image(
      painter = bitmapPainter as BitmapPainter,
      contentDescription = contentDescription,
      modifier = modifier
    )
  }
}