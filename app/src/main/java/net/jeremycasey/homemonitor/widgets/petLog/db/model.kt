package net.jeremycasey.homemonitor.widgets.petLog.db

import org.joda.time.DateTime
import org.joda.time.LocalTime

data class Animal(
  val id: String,
  val name: String,
  val type: String,
  val seq: Int,
)

data class PetMeal(
  val id: String,
  val animalId: String,
  val foodId: String,
  val mealId: String,
)

data class Food(
  val id: String,
  val name: String,
  val shortName: String,
  val seq: Int,
)

data class Meal(
  val id: String,
  val name: String,
  val timeOfDay: LocalTime,
)

data class Log(
  val id: String,
  val petMealId: String,
  val time: DateTime,
)
