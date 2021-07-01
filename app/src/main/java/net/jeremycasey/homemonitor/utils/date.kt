package net.jeremycasey.homemonitor.utils

import org.joda.time.DateTime
import org.joda.time.Period

fun toRelativeDateString(date: DateTime, now: DateTime): String {
  val period = Period(date, now)
  val duration = period.toStandardDuration()

  if (duration.standardSeconds < 60L) {
    return "Just now"
  } else if (duration.standardMinutes == 1L) {
    return "A minute ago"
  } else if (duration.standardMinutes < 60L) {
    return "${duration.standardMinutes} minutes ago"
  } else if (duration.standardHours == 1L) {
    return "An hour ago"
  } else if (duration.standardHours < 24L) {
    return "${duration.standardMinutes} hours ago"
  } else if (duration.standardDays == 1L) {
    return "A day ago"
  } else {
    return "${duration.standardDays} days ago"
  }
}

fun secondsToMs(seconds: Long): Long {
  return seconds * 1000
}

fun hoursToMs(hours: Long): Long {
  return hours * 60 * 60 * 1000
}

fun minutesToMs(mins: Long): Long {
  return mins * 60 * 1000
}

fun getTimeRemaining(date: DateTime, now: DateTime): String {
  val period = Period(now, date)
  val duration = period.toStandardDuration()

  if (duration.standardSeconds <= 0) {
    return "Now"
  } else if (duration.standardSeconds < 60) {
    return "<1"
  } else if (duration.standardMinutes < 60) {
    return "${duration.standardMinutes}"
  } else {
    return date.toString("HH:mm")
  }
}