package net.jeremycasey.homemonitor.db

import android.content.Context
import androidx.room.*

@Database(entities = arrayOf(PlAnimalsDao::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun plAnimalsDao(): PlAnimalsDao
}

fun initDb(applicationContext: Context): AppDatabase {
  return Room.databaseBuilder(
    applicationContext,
    AppDatabase::class.java, "database-name"
  ).build()
}