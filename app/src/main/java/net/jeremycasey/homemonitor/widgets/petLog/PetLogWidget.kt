package net.jeremycasey.homemonitor.widgets.petLog

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

val mockSubjectPeriods = petPeriods
val mockAnimals = subjects
val mockPeriods = periods
val mockActivities = activities

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
  private val _subjects = MutableLiveData<List<Subject>?>(null)
  val subjects: LiveData<List<Subject>?> = _subjects

  private val _periods = MutableLiveData<List<Period>?>(null)
  val periods: LiveData<List<Period>?> = _periods

  private val _activities = MutableLiveData<List<Activity>?>(null)
  val activities: LiveData<List<Activity>?> = _activities

  private val _petPeriods = MutableLiveData<List<SubjectPeriod>?>(null)
  val petPeriods: LiveData<List<SubjectPeriod>?> = _petPeriods

//  private val _currentPetLog = MutableLiveData<CurrentPetLog?>(null)
//  val currentPetLog: LiveData<CurrentPetLog?> = _currentPetLog

  private val _context = context

  private var _dbHelper: PetLogDbHelper? = null

  fun onPetDataRequired() {
    if (_dbHelper == null) {
      _dbHelper = PetLogDbHelper(_context)
    }
    val db = _dbHelper!!.readableDatabase
    _subjects.value = getAllSubjects(db)
    _periods.value = getAllPeriods(db)
    _activities.value = getAllActivities(db)
    _petPeriods.value = getAllSubjectPeriods(db)
    // TODO error handling
  }
}

@Composable
fun PetLogWidget(viewModel: PetLogWidgetViewModel) {
  val subjects by viewModel.subjects.observeAsState()
  val activities by viewModel.activities.observeAsState()
  val petPeriods by viewModel.petPeriods.observeAsState()
  val periods by viewModel.periods.observeAsState()

  LaunchedEffect("") {
    viewModel.onPetDataRequired()
  }

  PetLogWidgetView(subjects, periods, activities, petPeriods)
}

@Composable
fun PetLogWidgetView(subjects: List<Subject>?, periods: List<Period>?,
                     activities: List<Activity>?, petPeriods: List<SubjectPeriod>?) {
  if (petPeriods == null || subjects == null || periods == null || activities == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }


  WidgetCard {
    Row {
      Box(modifier = Modifier.width(80.dp))
      periods.map {period ->
        Text(
          period.name,
          modifier = Modifier.width(150.dp).padding(0.dp, 0.dp, 0.dp, 5.dp),
          style = MaterialTheme.typography.subtitle2
        )
      }
    }
    subjects.map {subject ->
      Row {
        Text(subject.name, modifier = Modifier.width(80.dp))
        periods.map { period ->
          val pms = petPeriods.filter { pm -> pm.periodId == period.id && pm.subjectId == subject.id }
          Row (modifier = Modifier.width(150.dp)) {
            pms.map { pm ->
              val activity = findActivity(activities, pm.activityId)
              Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp, 0.dp)) {
                Checkbox(checked = false, onCheckedChange = { }, modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
                Text("${activity?.shortName}")
              }
            }
          }
        }
      }
    }
  }
}

private fun findActivity(
  activities: List<Activity>,
  activityId: String
) = activities.find { f -> f.id == activityId }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    PetLogWidgetView(mockAnimals, mockPeriods, mockActivities, mockSubjectPeriods)
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
