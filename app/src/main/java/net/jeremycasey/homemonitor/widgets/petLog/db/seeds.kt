package net.jeremycasey.homemonitor.widgets.petLog.db

import org.joda.time.LocalTime

val billy = Animal(
  id = "101",
  name = "Billy",
  type = "dog",
  seq = 0,
)
val ginger = Animal(
  id = "102",
  name = "Ginger",
  type = "cat",
  seq = 1,
)
val animals = listOf(
  billy,
  ginger
)

val wetCatFood = Food(
  id = "201",
  name = "Wet Cat Food",
  shortName = "Wet",
  seq = 0,
)
val dryCatFood = Food(
  id = "202",
  name = "Dry Cat Food",
  shortName = "Dry",
  seq = 1,
)
val dogFood = Food(
  id = "203",
  name = "Dog food",
  shortName = "Food",
  seq = 0,
)
val foods = listOf(
  wetCatFood,
  dryCatFood,
  dogFood
)

val breakfast = Meal(
  id = "301",
  name = "Breakfast",
  timeOfDay = LocalTime(7, 0),
)
val dinner = Meal(
  id = "302",
  name = "Dinner",
  timeOfDay = LocalTime(17, 30),
)
val meals = listOf(
  breakfast,
  dinner
)

val petMeals = listOf(
  PetMeal(
    id = "401",
    animalId = ginger.id,
    foodId = wetCatFood.id,
    mealId = breakfast.id,
  ),
  PetMeal(
    id = "402",
    animalId = ginger.id,
    foodId = dryCatFood.id,
    mealId = breakfast.id,
  ),
  PetMeal(
    id = "403",
    animalId = ginger.id,
    foodId = wetCatFood.id,
    mealId = dinner.id,
  ),
  PetMeal(
    id = "404",
    animalId = ginger.id,
    foodId = dryCatFood.id,
    mealId = dinner.id,
  ),
  PetMeal(
    id = "406",
    animalId = billy.id,
    foodId = dogFood.id,
    mealId = dinner.id,
  )
)