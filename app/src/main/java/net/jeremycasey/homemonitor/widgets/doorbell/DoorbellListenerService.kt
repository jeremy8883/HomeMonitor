package net.jeremycasey.homemonitor.widgets.doorbell

import android.content.Intent
import android.graphics.Bitmap
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.joda.time.DateTime

class DoorbellListenerService : NotificationListenerService() {
  fun keySetToString(keySet: Set<String>): String {
    var ret = ""
    keySet.forEach { s -> ret += s + "," }
    return ret
  }

  override fun onNotificationPosted(sbn: StatusBarNotification) {
    if (sbn.packageName != "com.ring.answer") {
      return
    }

    cancelNotification(sbn.key)

    val title = sbn.notification.extras.getString("android.title") ?: return

    val eventType = when {
      title.indexOf("Someone is at your") != -1 -> "ring"
      title.indexOf("There is motion at your ") != -1 -> "motion"
      else -> null
    } ?: return

    val picture = sbn.notification.extras.get("android.picture") ?: return

    val i = Intent("net.jeremycasey.homemonitor.DOORBELL_NOTIFICATION_LISTENER")
    i.putExtra("picture", picture as Bitmap)
    i.putExtra("title", title)
    i.putExtra("eventType", eventType)
    i.putExtra("contentIntent", sbn.notification.contentIntent)
    i.putExtra("dateTime", DateTime.now().toString())
    sendBroadcast(i)

    if (eventType === "ring") {
      sbn.notification.contentIntent.send()
    }

//    println(
//      "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t"
//          + sbn.packageName + // com.ring.answer
//          "\nFull screen intent: " + sbn.notification.fullScreenIntent +
//          "\nExtras: " + keySetToString(sbn.notification.extras.keySet()) +
//          "\nContent intent: " + sbn.notification.contentIntent +
//          "\nandroid.title: " + sbn.notification.extras.get("android.title") + // "🔔 Someone is at your Front Door" or "There is motion at your Front Door"
//          "\nandroid.reduced.images: " + sbn.notification.extras.get("android.reduced.images") +
//          "\nandroid.textLines: " + sbn.notification.extras.get("android.textLines")?.toString() +
//          "\nandroid.subText: " + sbn.notification.extras.get("android.subText") +
//          "\nandroid.infoText: " + sbn.notification.extras.get("android.infoText") +
//          "\nandroid.largeIcon: " + sbn.notification.extras.get("android.largeIcon") +
//          "\nandroid.template: " + sbn.notification.extras.get("android.template") +
//          "\nandroid.showChronometer: " + sbn.notification.extras.get("android.showChronometer") +
//          "\nandroid.text: " + sbn.notification.extras.get("android.text") +
//          "\nandroid.progress: " + sbn.notification.extras.get("android.progress") +
//          "\nandroid.progressMax: " + sbn.notification.extras.get("android.progressMax") +
//          "\nandroid.appInfo: " + sbn.notification.extras.get("android.appInfo") +
//          "\nandroid.picture: " + sbn.notification.extras.get("android.picture") + // android.graphics.Bitmap
//          "\nandroid.contains.customView: " + sbn.notification.extras.get("android.contains") +
//          "\nandroid.showWhen: " + sbn.notification.extras.get("android.showWhen") +
//          "\nandroid.largeIcon: " + sbn.notification.extras.get("android.largeIcon") +
//          "\nandroid.infoText: " + sbn.notification.extras.get("android.infoText") +
//          "\nandroid.progressIndeterminate: " + sbn.notification.extras.get("android.progressIndeterminate") +
//          "\nandroid.remoteInputHistory: " + sbn.notification.extras.get("android.remoteInputHistory") +
//          "\nlargeIcon: " + sbn.notification.getLargeIcon() +
//          "\nsmallIcon: " + sbn.notification.smallIcon +
//          "\nbubbleMetadata: " + sbn.notification.bubbleMetadata +
//          "\nbigContentView: " + sbn.notification.bigContentView
//    )
  }
}