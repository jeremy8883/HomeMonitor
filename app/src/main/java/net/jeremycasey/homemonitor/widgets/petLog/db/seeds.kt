package net.jeremycasey.homemonitor.widgets.petLog.db

import org.joda.time.LocalTime

val billy = Subject(
  id = "101",
  name = "Billy",
  type = "dog",
  seq = 0,
)
val ginger = Subject(
  id = "102",
  name = "Ginger",
  type = "cat",
  seq = 1,
)
val subjects = listOf(
  billy,
  ginger
)

val wetCatFood = Activity(
  id = "201",
  name = "Wet Cat Activity",
  shortName = "Wet",
  seq = 0,
)
val dryCatFood = Activity(
  id = "202",
  name = "Dry Cat Activity",
  shortName = "Dry",
  seq = 1,
)
val dogFood = Activity(
  id = "203",
  name = "Dog food",
  shortName = "Activity",
  seq = 0,
)
val walk = Activity(
  id = "204",
  name = "Walk",
  shortName = "Walk",
  seq = 0,
)
val activities = listOf(
  wetCatFood,
  dryCatFood,
  dogFood,
  walk,
)

val morning = Period(
  id = "301",
  name = "Morning",
  timeOfDay = LocalTime(7, 0),
)
val afternoon = Period(
  id = "302",
  name = "Afternoon",
  timeOfDay = LocalTime(12, 0),
)
val evening = Period(
  id = "303",
  name = "Evening",
  timeOfDay = LocalTime(17, 30),
)
val periods = listOf(
  morning,
  afternoon,
  evening,
)

val petPeriods = listOf(
  SubjectPeriod(
    id = "401",
    subjectId = ginger.id,
    activityId = wetCatFood.id,
    periodId = morning.id,
  ),
  SubjectPeriod(
    id = "402",
    subjectId = ginger.id,
    activityId = dryCatFood.id,
    periodId = morning.id,
  ),
  SubjectPeriod(
    id = "403",
    subjectId = ginger.id,
    activityId = wetCatFood.id,
    periodId = evening.id,
  ),
  SubjectPeriod(
    id = "404",
    subjectId = ginger.id,
    activityId = dryCatFood.id,
    periodId = evening.id,
  ),
  SubjectPeriod(
    id = "406",
    subjectId = billy.id,
    activityId = dogFood.id,
    periodId = evening.id,
  ),
  SubjectPeriod(
    id = "407",
    subjectId = billy.id,
    activityId = walk.id,
    periodId = afternoon.id,
  )
)