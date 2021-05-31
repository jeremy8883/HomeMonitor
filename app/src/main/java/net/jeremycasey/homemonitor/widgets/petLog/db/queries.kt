package net.jeremycasey.homemonitor.widgets.petLog.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.joda.time.DateTime
import org.joda.time.LocalTime

// These functions are obviously quite verbose and repetitive. I did try using Room, but was plagued
// with errors, so I decided to keep everything simple. I'll give Room another go some other time.

fun getAllAnimals(db: SQLiteDatabase): List<Animal> {
  val cursor = db.rawQuery("SELECT * from Animal", arrayOf())

  val animals = mutableListOf<Animal>()
  with(cursor) {
    while (moveToNext()) {
      animals.add(Animal(
        id = getString(getColumnIndexOrThrow("id")),
        name = getString(getColumnIndexOrThrow("name")),
        type = getString(getColumnIndexOrThrow("type")),
        seq = getInt(getColumnIndexOrThrow("seq")),
      ))
    }
  }
  return animals
}

fun insertAnimal(db: SQLiteDatabase, animal: Animal) {
  val values = ContentValues().apply {
    put("id", animal.id)
    put("name", animal.name)
    put("type", animal.type)
    put("seq", animal.seq)
  }
  db.insert("Animal", null, values)
}

fun getAllFoods(db: SQLiteDatabase): List<Food> {
  val cursor = db.rawQuery("SELECT * from Food ORDER BY seq", arrayOf())

  val foods = mutableListOf<Food>()
  with(cursor) {
    while (moveToNext()) {
      foods.add(Food(
        id = getString(getColumnIndexOrThrow("id")),
        name = getString(getColumnIndexOrThrow("name")),
        shortName = getString(getColumnIndexOrThrow("shortName")),
        seq = getInt(getColumnIndexOrThrow("seq")),
      ))
    }
  }
  return foods
}

fun insertFood(db: SQLiteDatabase, food: Food) {
  val values = ContentValues().apply {
    put("id", food.id)
    put("name", food.name)
    put("shortName", food.shortName)
    put("seq", food.seq)
  }
  db.insert("Food", null, values)
}

fun getAllMeals(db: SQLiteDatabase): List<Meal> {
  val cursor = db.rawQuery("SELECT * from Meal ORDER BY timeOfDay", arrayOf())

  val meals = mutableListOf<Meal>()
  with(cursor) {
    while (moveToNext()) {
      meals.add(Meal(
        id = getString(getColumnIndexOrThrow("id")),
        name = getString(getColumnIndexOrThrow("name")),
        timeOfDay = LocalTime(getString(getColumnIndexOrThrow("timeOfDay"))),
      ))
    }
  }
  return meals
}

fun insertMeal(db: SQLiteDatabase, meal: Meal) {
  val values = ContentValues().apply {
    put("id", meal.id)
    put("name", meal.name)
    put("timeOfDay", meal.timeOfDay.toString())
  }
  db.insert("Meal", null, values)
}

fun getAllPetMeals(db: SQLiteDatabase): List<PetMeal> {
  val cursor = db.rawQuery("SELECT * from PetMeal", arrayOf())

  val petMeals = mutableListOf<PetMeal>()
  with(cursor) {
    while (moveToNext()) {
      petMeals.add(PetMeal(
        id = getString(getColumnIndexOrThrow("id")),
        animalId = getString(getColumnIndexOrThrow("animalId")),
        foodId = getString(getColumnIndexOrThrow("foodId")),
        mealId = getString(getColumnIndexOrThrow("mealId")),
      ))
    }
  }
  return petMeals
}

fun insertPetMeal(db: SQLiteDatabase, petMeal: PetMeal) {
  val values = ContentValues().apply {
    put("id", petMeal.id)
    put("animalId", petMeal.animalId)
    put("foodId", petMeal.foodId)
    put("mealId", petMeal.mealId)
  }
  db.insert("PetMeal", null, values)
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
        petMealId = getString(getColumnIndexOrThrow("petMealId")),
        time = DateTime(getString(getColumnIndexOrThrow("timeOfDay"))),
      ))
    }
  }
  return logs
}

fun insertLog(db: SQLiteDatabase, log: Log) {
  val values = ContentValues().apply {
    put("id", log.id)
    put("petMealId", log.petMealId)
    put("time", log.time.toString())
  }
  db.insert("Log", null, values)
}
