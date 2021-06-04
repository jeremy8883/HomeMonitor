package net.jeremycasey.homemonitor.utils

import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.DateTime
import org.joda.time.Period

fun toRelativeDateString(date: DateTime, now: DateTime): String {
  val period = Period(date, now)

  if (period.toStandardDuration().standardSeconds <= 60) {
    return "Just now"
  }

  val formatter = PeriodFormatterBuilder()
    .appendSeconds().appendSuffix(" seconds ago\n")
    .appendMinutes().appendSuffix(" minutes ago\n")
    .appendHours().appendSuffix(" hours ago\n")
    .appendDays().appendSuffix(" days ago\n")
    .appendWeeks().appendSuffix(" weeks ago\n")
    .appendMonths().appendSuffix(" months ago\n")
    .appendYears().appendSuffix(" years ago\n")
    .printZeroNever()
    .toFormatter()

  return formatter.print(period)
}

fun hoursToMs(hours: Long): Long {
  return hours * 60 * 60 * 1000
}