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
import androidx.compose.runtime.remember
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
import net.jeremycasey.homemonitor.composables.LoadingPanel
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.composables.WithCurrentTime
import net.jeremycasey.homemonitor.utils.hoursToMs
import org.joda.time.DateTime
import java.util.*

val mockActivityPeriods = petPeriods
val mockSubjects = subjects
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

  private val _petPeriods = MutableLiveData<List<ActivityPeriod>?>(null)
  val petPeriods: LiveData<List<ActivityPeriod>?> = _petPeriods

  private val _logs = MutableLiveData<MutableList<Log>?>(null)
  val logs: LiveData<ArrayList<Log>?> = _logs as LiveData<ArrayList<Log>?>

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
    _petPeriods.value = getAllActivityPeriods(db)
    _logs.value = getTodaysLogs(db, DateTime.now())
    // TODO error handling
  }

  fun onLogActivity(activityPeriodId: String) {
    if (_dbHelper == null) {
      _dbHelper = PetLogDbHelper(_context)
    }
    val db = _dbHelper!!.writableDatabase
    val log = Log(
      id = UUID.randomUUID().toString(),
      activityPeriodId = activityPeriodId,
      dateTime = DateTime.now()
    )
    insertLog(db, log)

    _logs.value = _logs.value?.plus(log) as MutableList<Log>?

    // TODO error handling
  }

  fun onRemoveLog(logId: String) {
    if (_dbHelper == null) {
      _dbHelper = PetLogDbHelper(_context)
    }
    val db = _dbHelper!!.writableDatabase
    removeLog(db, logId)

    _logs.value = _logs.value?.filter { l -> l.id != logId } as MutableList<Log>?

    // TODO error handling
  }
}

@Composable
fun PetLogWidget(viewModel: PetLogWidgetViewModel) {
  val subjects by viewModel.subjects.observeAsState()
  val activities by viewModel.activities.observeAsState()
  val petPeriods by viewModel.petPeriods.observeAsState()
  val periods by viewModel.periods.observeAsState()
  val logs by viewModel.logs.observeAsState()

  LaunchedEffect("") {
    viewModel.onPetDataRequired()
  }

  WithCurrentTime(hoursToMs(1)) { now ->
    PetLogWidgetView(
      subjects, periods, activities, petPeriods, logs, now,
      { apId -> viewModel.onLogActivity(apId) },
      { logId -> viewModel.onRemoveLog(logId) }
    )
  }
}

@Composable
fun PetLogWidgetView(
  subjects: List<Subject>?, periods: List<Period>?,
  activities: List<Activity>?, activityPeriods: List<ActivityPeriod>?,
  logs: List<Log>?,
  now: DateTime,
  onLogActivity: (activityPeriodId: String) -> Any,
  onRemoveLog: (activityPeriodId: String) -> Any
) {
  if (activityPeriods == null || subjects == null || periods == null ||
    activities == null || logs == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }

  val todaysLogs = remember(logs, now) {
    logs.filter { it.dateTime.withTimeAtStartOfDay().millis == now.withTimeAtStartOfDay().millis }
  }

  WidgetCard {
    Row {
      Box(modifier = Modifier.width(80.dp))
      periods.map {period ->
        Text(
          period.name,
          modifier = Modifier
            .width(150.dp)
            .padding(0.dp, 0.dp, 0.dp, 5.dp),
          style = MaterialTheme.typography.subtitle2
        )
      }
    }
    subjects.map {subject ->
      Row {
        Text(subject.name, modifier = Modifier.width(80.dp))
        periods.map { period ->
          val aps = activityPeriods.filter { pm -> pm.periodId == period.id && pm.subjectId == subject.id }
          Row (modifier = Modifier.width(150.dp)) {
            aps.map { ap ->
              val activity = findActivity(activities, ap.activityId)
              val log = findLogByActivityPeriod(todaysLogs, ap.id)
              Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp, 0.dp)) {
                Checkbox(
                  checked = log != null,
                  onCheckedChange = {
                    if (log == null) onLogActivity(ap.id)
                    else onRemoveLog(log.id)
                  },
                  modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp)
                )
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

private fun findLogByActivityPeriod(
  logs: List<Log>,
  activityPeriodId: String
) = logs.find { l -> l.activityPeriodId == activityPeriodId }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    PetLogWidgetView(
      mockSubjects, mockPeriods, mockActivities, mockActivityPeriods, listOf(),
      DateTime.now(), { }, { }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    PetLogWidgetView(null, null, null, null, null,
      DateTime.now(), { }, { })
  }
}

//@Preview(showBackground = true)
//@Composable
//fun ErrorPreview() {
//  HomeMonitorTheme {
//    PetLogWidgetView(null, Exception("This is an error"), { })
//  }
//}
