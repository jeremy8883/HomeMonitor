package net.jeremycasey.homemonitor.widgets.petLog.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.joda.time.DateTime
import org.joda.time.LocalTime

// These functions are obviously quite verbose and repetitive. I did try using Room, but was plagued
// with errors, so I decided to keep everything simple. I'll give Room another go some other time.

fun getAllSubjects(db: SQLiteDatabase): List<Subject> {
  val cursor = db.rawQuery("SELECT * from Subject", arrayOf())

  val subjects = mutableListOf<Subject>()
  with(cursor) {
    while (moveToNext()) {
      subjects.add(Subject(
        id = getString(getColumnIndexOrThrow("id")),
        name = getString(getColumnIndexOrThrow("name")),
        type = getString(getColumnIndexOrThrow("type")),
        seq = getInt(getColumnIndexOrThrow("seq")),
      ))
    }
  }
  return subjects
}

fun insertSubject(db: SQLiteDatabase, subject: Subject) {
  val values = ContentValues().apply {
    put("id", subject.id)
    put("name", subject.name)
    put("type", subject.type)
    put("seq", subject.seq)
  }
  db.insert("Subject", null, values)
}

fun getAllActivities(db: SQLiteDatabase): List<Activity> {
  val cursor = db.rawQuery("SELECT * from Activity ORDER BY seq", arrayOf())

  val activities = mutableListOf<Activity>()
  with(cursor) {
    while (moveToNext()) {
      activities.add(Activity(
        id = getString(getColumnIndexOrThrow("id")),
        name = getString(getColumnIndexOrThrow("name")),
        shortName = getString(getColumnIndexOrThrow("shortName")),
        seq = getInt(getColumnIndexOrThrow("seq")),
      ))
    }
  }
  return activities
}

fun insertActivity(db: SQLiteDatabase, activity: Activity) {
  val values = ContentValues().apply {
    put("id", activity.id)
    put("name", activity.name)
    put("shortName", activity.shortName)
    put("seq", activity.seq)
  }
  db.insert("Activity", null, values)
}

fun getAllPeriods(db: SQLiteDatabase): List<Period> {
  val cursor = db.rawQuery("SELECT * from Period ORDER BY timeOfDay", arrayOf())

  val periods = mutableListOf<Period>()
  with(cursor) {
    while (moveToNext()) {
      periods.add(Period(
        id = getString(getColumnIndexOrThrow("id")),
        name = getString(getColumnIndexOrThrow("name")),
        timeOfDay = LocalTime(getString(getColumnIndexOrThrow("timeOfDay"))),
      ))
    }
  }
  return periods
}

fun insertPeriod(db: SQLiteDatabase, period: Period) {
  val values = ContentValues().apply {
    put("id", period.id)
    put("name", period.name)
    put("timeOfDay", period.timeOfDay.toString())
  }
  db.insert("Period", null, values)
}

fun getAllSubjectPeriods(db: SQLiteDatabase): List<SubjectPeriod> {
  val cursor = db.rawQuery("SELECT * from SubjectPeriod", arrayOf())

  val petPeriods = mutableListOf<SubjectPeriod>()
  with(cursor) {
    while (moveToNext()) {
      petPeriods.add(SubjectPeriod(
        id = getString(getColumnIndexOrThrow("id")),
        subjectId = getString(getColumnIndexOrThrow("subjectId")),
        activityId = getString(getColumnIndexOrThrow("activityId")),
        periodId = getString(getColumnIndexOrThrow("periodId")),
      ))
    }
  }
  return petPeriods
}

fun insertSubjectPeriod(db: SQLiteDatabase, petPeriod: SubjectPeriod) {
  val values = ContentValues().apply {
    put("id", petPeriod.id)
    put("subjectId", petPeriod.subjectId)
    put("activityId", petPeriod.activityId)
    put("periodId", petPeriod.periodId)
  }
  db.insert("SubjectPeriod", null, values)
}

fun getTodaysLogs(db: SQLiteDatabase, now: DateTime): List<Log> {
  val cursor = db.rawQuery(
    "SELECT * from Log where date >= ? AND date < ?",
    arrayOf(
      now.withTimeAtStartOfDay().toString(),
      now.plusDays(1).withTimeAtStartOfDay().toString()
    ))

  val logs = mutableListOf<Log>()
  with(cursor) {
    while (moveToNext()) {
      logs.add(Log(
        id = getString(getColumnIndexOrThrow("id")),
        petPeriodId = getString(getColumnIndexOrThrow("petPeriodId")),
        time = DateTime(getString(getColumnIndexOrThrow("timeOfDay"))),
      ))
    }
  }
  return logs
}

fun insertLog(db: SQLiteDatabase, log: Log) {
  val values = ContentValues().apply {
    put("id", log.id)
    put("petPeriodId", log.petPeriodId)
    put("time", log.time.toString())
  }
  db.insert("Log", null, values)
}
