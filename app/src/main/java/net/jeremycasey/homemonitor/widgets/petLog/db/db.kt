package net.jeremycasey.homemonitor.widgets.petLog.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private fun createTables(db: SQLiteDatabase) {
  db.execSQL("CREATE TABLE Subject (" +
      "id TEXT PRIMARY KEY," +
      "name TEXT NOT NULL," +
      "type TEXT NOT NULL," +
      "seq INTEGER NOT NULL);")
  db.execSQL("CREATE TABLE Activity (" +
      "id TEXT PRIMARY KEY," +
      "name TEXT NOT NULL," +
      "shortName TEXT NOT NULL," +
      "seq INTEGER NOT NULL);")
  db.execSQL("CREATE TABLE Period (" +
      "id TEXT PRIMARY KEY," +
      "name TEXT NOT NULL," +
      "timeOfDay DATE NOT NULL);")
  db.execSQL("CREATE TABLE ActivityPeriod (" +
      "id TEXT PRIMARY KEY," +
      "subjectId TEXT NOT NULL," +
      "activityId TEXT NOT NULL," +
      "periodId TEXT NOT NULL," +
      "FOREIGN KEY (subjectId) REFERENCES Subject(id)," +
      "FOREIGN KEY (activityId) REFERENCES Activity(id)," +
      "FOREIGN KEY (periodId) REFERENCES Period(id));")
  db.execSQL("CREATE TABLE Log (" +
      "id TEXT PRIMARY KEY," +
      "activityPeriodId TEXT NOT NULL," +
      "dateTime DATE NOT NULL," +
      "FOREIGN KEY (activityPeriodId) REFERENCES ActivityPeriod(id));")
}

private fun dropTables(db: SQLiteDatabase) {
  db.execSQL("DROP TABLE IF EXISTS ActivityPeriod")
  db.execSQL("DROP TABLE IF EXISTS Activity")
  db.execSQL("DROP TABLE IF EXISTS Period")
  db.execSQL("DROP TABLE IF EXISTS Subject")
  db.execSQL("DROP TABLE IF EXISTS Log")
}

private fun insertSeedData(db: SQLiteDatabase) {
  // Personal entries. In future, we could make this customizable in the UI.
  subjects.forEach { insertSubject(db, it) }
  activities.forEach { insertActivity(db, it) }
  periods.forEach { insertPeriod(db, it) }
  petPeriods.forEach { insertActivityPeriod(db, it) }
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
    const val DATABASE_VERSION = 12
    const val DATABASE_NAME = "PetLog"
  }
}
