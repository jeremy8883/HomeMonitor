package net.jeremycasey.homemonitor.widgets.petLog.db

import org.joda.time.DateTime
import org.joda.time.LocalTime

data class Subject(
  val id: String,
  val name: String,
  val type: String,
  val seq: Int,
)

data class ActivityPeriod(
  val id: String,
  val subjectId: String,
  val activityId: String,
  val periodId: String,
)

data class Activity(
  val id: String,
  val name: String,
  val shortName: String,
  val seq: Int,
)

data class Period(
  val id: String,
  val name: String,
  val timeOfDay: LocalTime,
)

data class Log(
  val id: String,
  val activityPeriodId: String,
  val dateTime: DateTime,
)
