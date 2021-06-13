package net.jeremycasey.homemonitor.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.Window
import android.util.DisplayMetrics
import android.util.Size


fun openApp(context: Context, packageName: String) {
  val launchIntent: Intent? = context.getPackageManager()
    .getLaunchIntentForPackage(packageName)
  context.startActivity(launchIntent)
}

fun hideSystemUi(window: Window) {
  // Enables regular immersive mode.
  // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
  // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
  window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
      // Set the content to appear under the system bars so that the
      // content doesn't resize when the system bars hide and show.
      or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      // Hide the nav bar and status bar
      or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
      or View.SYSTEM_UI_FLAG_FULLSCREEN)
}

fun getScreenSize(activity: Activity): Size {
  val displayMetrics = DisplayMetrics()
  activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
  val height = displayMetrics.heightPixels
  val width = displayMetrics.widthPixels
  return Size(width, height)
}
