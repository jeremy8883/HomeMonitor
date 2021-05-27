package net.jeremycasey.homemonitor.utils

import com.google.gson.Gson

fun printObject(obj: Any) {
  val gson = Gson()
  println(gson.toJson(obj).toString());
}
