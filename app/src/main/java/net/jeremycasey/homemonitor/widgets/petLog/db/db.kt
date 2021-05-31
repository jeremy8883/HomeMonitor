package net.jeremycasey.homemonitor.widgets.petLog.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private fun createTables(db: SQLiteDatabase) {
  db.execSQL("CREATE TABLE Animal (" +
      "id TEXT PRIMARY KEY," +
      "name TEXT NOT NULL," +
      "type TEXT NOT NULL," +
      "seq INTEGER NOT NULL);")
  db.execSQL("CREATE TABLE Food (" +
      "id TEXT PRIMARY KEY," +
      "name TEXT NOT NULL," +
      "shortName TEXT NOT NULL," +
      "seq INTEGER NOT NULL);")
  db.execSQL("CREATE TABLE Meal (" +
      "id TEXT PRIMARY KEY," +
      "name TEXT NOT NULL," +
      "timeOfDay DATE NOT NULL);")
  db.execSQL("CREATE TABLE PetMeal (" +
      "id TEXT PRIMARY KEY," +
      "animalId TEXT NOT NULL," +
      "foodId TEXT NOT NULL," +
      "mealId TEXT NOT NULL," +
      "FOREIGN KEY (animalId) REFERENCES Animal(id)," +
      "FOREIGN KEY (foodId) REFERENCES Food(id)," +
      "FOREIGN KEY (mealId) REFERENCES Meal(id));")
  db.execSQL("CREATE TABLE Log (" +
      "id TEXT PRIMARY KEY," +
      "petMealId TEXT NOT NULL," +
      "time DATE NOT NULL," +
      "FOREIGN KEY (petMealId) REFERENCES PetMeal(id));")
}

private fun dropTables(db: SQLiteDatabase) {
  db.execSQL("DROP TABLE IF EXISTS PetMeal")
  db.execSQL("DROP TABLE IF EXISTS Food")
  db.execSQL("DROP TABLE IF EXISTS Meal")
  db.execSQL("DROP TABLE IF EXISTS Animal")
  db.execSQL("DROP TABLE IF EXISTS Log")
}

private fun insertSeedData(db: SQLiteDatabase) {
  // Personal entries. In future, we could make this customizable in the UI.
  animals.forEach { insertAnimal(db, it) }
  foods.forEach { insertFood(db, it) }
  meals.forEach { insertMeal(db, it) }
  petMeals.forEach { insertPetMeal(db, it) }
}

class PetLogDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  override fun onCreate(db: SQLiteDatabase) {
    try {
      db.beginTransaction()
      createTables(db)
      insertSeedData(db)
      db.setTransactionSuccessful()
    } finally {
      db.endTransaction()
    }
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    dropTables(db)
    onCreate(db)
  }

  override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    onUpgrade(db, oldVersion, newVersion)
  }

  companion object {
    const val DATABASE_VERSION = 4
    const val DATABASE_NAME = "PetLog"
  }
}
