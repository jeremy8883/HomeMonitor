package net.jeremycasey.homemonitor.utils

import android.content.Context
import android.content.Intent

fun openApp(context: Context, packageName: String) {
  val launchIntent: Intent? = context.getPackageManager()
    .getLaunchIntentForPackage(packageName)
  context.startActivity(launchIntent)
}
