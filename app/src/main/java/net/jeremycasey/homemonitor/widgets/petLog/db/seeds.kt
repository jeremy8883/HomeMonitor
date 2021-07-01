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
val dogFood = Activity(
  id = "203",
  name = "Dog food",
  shortName = "Dinner",
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
  dogFood,
  walk,
)

val morning = Period(
  id = "301",
  name = "Morning",
  timeOfDay = LocalTime(7, 0),
)
val evening = Period(
  id = "303",
  name = "Evening",
  timeOfDay = LocalTime(17, 30),
)
val periods = listOf(
  morning,
  evening,
)

val petPeriods = listOf(
  ActivityPeriod(
    id = "401",
    subjectId = ginger.id,
    activityId = wetCatFood.id,
    periodId = morning.id,
  ),
  ActivityPeriod(
    id = "403",
    subjectId = ginger.id,
    activityId = wetCatFood.id,
    periodId = evening.id,
  ),
  ActivityPeriod(
    id = "406",
    subjectId = billy.id,
    activityId = dogFood.id,
    periodId = evening.id,
  ),
  ActivityPeriod(
    id = "407",
    subjectId = billy.id,
    activityId = walk.id,
    periodId = morning.id,
  )
)