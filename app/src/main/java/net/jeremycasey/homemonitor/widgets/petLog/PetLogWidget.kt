package net.jeremycasey.homemonitor.widgets.petLog

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.widgets.petLog.db.*
import net.jeremycasey.homemonitor.widgets.shared.LoadingPanel
import net.jeremycasey.homemonitor.widgets.shared.WidgetCard

val mockPetMeals = petMeals
val mockAnimals = animals
val mockMeals = meals
val mockFoods = foods

class PetLogWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return PetLogWidgetViewModel(_context) as T
  }
}

class PetLogWidgetViewModel(context: Context) : ViewModel() {
  private val _animals = MutableLiveData<List<Animal>?>(null)
  val animals: LiveData<List<Animal>?> = _animals

  private val _meals = MutableLiveData<List<Meal>?>(null)
  val meals: LiveData<List<Meal>?> = _meals

  private val _foods = MutableLiveData<List<Food>?>(null)
  val foods: LiveData<List<Food>?> = _foods

  private val _petMeals = MutableLiveData<List<PetMeal>?>(null)
  val petMeals: LiveData<List<PetMeal>?> = _petMeals

//  private val _currentPetLog = MutableLiveData<CurrentPetLog?>(null)
//  val currentPetLog: LiveData<CurrentPetLog?> = _currentPetLog

  private val _context = context

  private var _dbHelper: PetLogDbHelper? = null

  fun onPetDataRequired() {
    if (_dbHelper == null) {
      _dbHelper = PetLogDbHelper(_context)
    }
    val db = _dbHelper!!.readableDatabase
    _animals.value = getAllAnimals(db)
    _meals.value = getAllMeals(db)
    _foods.value = getAllFoods(db)
    _petMeals.value = getAllPetMeals(db)
    // TODO error handling
  }
}

@Composable
fun PetLogWidget(viewModel: PetLogWidgetViewModel) {
  val animals by viewModel.animals.observeAsState()
  val foods by viewModel.foods.observeAsState()
  val petMeals by viewModel.petMeals.observeAsState()
  val meals by viewModel.meals.observeAsState()

  LaunchedEffect("") {
    viewModel.onPetDataRequired()
  }

  PetLogWidgetView(animals, meals, foods, petMeals)
}

@Composable
fun PetLogWidgetView(animals: List<Animal>?, meals: List<Meal>?,
    foods: List<Food>?, petMeals: List<PetMeal>?) {
  if (petMeals == null || animals == null || meals == null || foods == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }


  WidgetCard {
    Row {
      Box(modifier = Modifier.width(80.dp))
      meals.map {meal ->
        Text(
          meal.name,
          modifier = Modifier.width(150.dp).padding(0.dp, 0.dp, 0.dp, 5.dp),
          style = MaterialTheme.typography.subtitle2
        )
      }
    }
    animals.map {animal ->
      Row {
        Text(animal.name, modifier = Modifier.width(80.dp))
        meals.map { meal ->
          val pms = petMeals.filter { pm -> pm.mealId == meal.id && pm.animalId == animal.id }
          Row (modifier = Modifier.width(150.dp)) {
            pms.map { pm ->
              val food = findFood(foods, pm.foodId)
              Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp, 0.dp)) {
                Checkbox(checked = false, onCheckedChange = { }, modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
                Text("${food?.shortName}")
              }
            }
          }
        }
      }
    }
  }
}

private fun findFood(
  foods: List<Food>,
  foodId: String
) = foods.find { f -> f.id == foodId }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    PetLogWidgetView(mockAnimals, mockMeals, mockFoods, mockPetMeals)
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    PetLogWidgetView(null, null, null, null)
  }
}

//@Preview(showBackground = true)
//@Composable
//fun ErrorPreview() {
//  HomeMonitorTheme {
//    PetLogWidgetView(null, Exception("This is an error"), { })
//  }
//}
