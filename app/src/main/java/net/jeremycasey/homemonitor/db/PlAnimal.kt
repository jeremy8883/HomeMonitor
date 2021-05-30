package net.jeremycasey.homemonitor.db

import androidx.room.*

@Entity
data class PlAnimal(
  @PrimaryKey val uid: Int,
  @ColumnInfo(name = "name") val name: String,
  @ColumnInfo(name = "type") val type: String,
  @ColumnInfo(name = "seq") val seq: Int,
)

//@Entity
//data class PlPetMeal(
//  @PrimaryKey val uid: Int,
//  @SecondaryKey(name = "foodId") val foodId: Int,
//  @SecondaryKey(name = "mealId") val mealId: Int,
//)
//
//@Entity
//data class PlFood(
//  @PrimaryKey val uid: Int,
//  @ColumnInfo(name = "name") val name: String,
//  @ColumnInfo(name = "seq") val seq: Int,
//)
//
//@Entity
//data class PlMeal(
//  @PrimaryKey val uid: Int,
//  @ColumnInfo(name = "name") val name: String,
//  @ColumnInfo(name = "timeOfDay") val timeOfDay: Date,
//)
//
//@Entity
//data class PlLog(
//  @PrimaryKey val uid: Int,
//  @SecondaryKey(name = "petMealId") val name: Int,
//  @ColumnInfo(name = "time") val time: Date,
//)

@Dao
interface PlAnimalsDao {
  @Query("SELECT * FROM PlAnimal")
  fun getAllAnimals(): List<PlAnimal>

//  @Query("SELECT * FROM PlPetMeal")
//  fun getAll(): List<PlPetMeal>
//
//  @Query("SELECT * FROM PlFood")
//  fun getAll(): List<PlPetMeal>
//
//  @Query("SELECT * FROM PlPetMeal WHERE uid IN (:userIds)")
//  fun loadAllByIds(userIds: IntArray): List<User>
//
//  @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//      "last_name LIKE :last LIMIT 1")
//  fun findByName(first: String, last: String): PlAnimal
//
//  @Insert
//  fun insertAll(vararg users: User)
//
//  @Delete
//  fun delete(user: User)
}
