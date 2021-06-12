package net.jeremycasey.homemonitor.widgets.lights

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.*
import net.jeremycasey.homemonitor.private.hueGroupIds
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.hoursToMs
import net.jeremycasey.homemonitor.widgets.lights.api.changeGroupState
import net.jeremycasey.homemonitor.widgets.lights.api.fetchGroups
import net.jeremycasey.homemonitor.widgets.weather.api.*

val mockLightGroups = mapOf(
  "1" to LightGroup(
    id = "1",
    name = "Living room",
    lights = listOf( "1", "12", "10" ),
//    sensors = listOf(),
    type = "Room",
    state = LightGroupState( allOn = false, anyOn = false ),
    recycle = false,
    `class` = "Living room",
    action = LightGroupAction(
      on = false,
      bri = 254,
      hue = 14957,
      sat = 141,
      effect = "none",
      xy = listOf(),
      ct = 366,
      alert = "select",
      colormode = "xy"
    ),
  ),
  "2" to LightGroup(
    id = "2",
    name = "Study",
    lights = listOf( "1", "5" ),
//    sensors = listOf(),
    type = "Room",
    state = LightGroupState( allOn = true, anyOn = true ),
    recycle = false,
    `class` = "Living room",
    action = LightGroupAction(
      on = true,
      bri = 254,
      hue = 65527,
      sat = 253,
      effect = "none",
      xy = listOf(0.4576, 0.4099),
      ct = 153,
      alert = "select",
      colormode = "hs"
    )
  ),
  "3" to LightGroup(
    id = "3",
    name = "Master bedroom",
    lights = listOf("14", "13", "6" ),
//    sensors = listOf(),
    type = "Room",
    state = LightGroupState(allOn = true, anyOn = true),
    recycle = false,
    `class` = "Bedroom",
    action = LightGroupAction(on = true, bri = 254, alert = "select"),
  )
)

class LightsWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return LightsWidgetViewModel(_context) as T
  }
}

class LightsWidgetViewModel(context: Context) : ViewModel() {
  private val _groups = MutableLiveData<Map<String, LightGroup>?>(null)
  val allGroups: LiveData<Map<String, LightGroup>?> = _groups

  private val _context = context

  private val _fetchError = MutableLiveData<Exception?>(null)
  val fetchError: LiveData<Exception?> = _fetchError

  fun onLightGroupsRequired() {
    _fetchError.value = null
    fetchGroups(
      _context,
      { lightGroups: Map<String, LightGroup> ->
        _groups.value = lightGroups
      },
      { error: Exception ->
        _fetchError.value = error
      }
    )
  }

  fun onToggleGroup(groupId: String, isOn: Boolean) {
    changeGroupState(
      groupId, LightGroupStateAction(on = isOn), _context,
      {
        onLightGroupsRequired()
      },
      {
        println(it)
      }
    )
  }
}

@Composable
fun LightsWidget(viewModel: LightsWidgetViewModel) {
  val allGroups by viewModel.allGroups.observeAsState()
  val fetchError by viewModel.fetchError.observeAsState()

  PollEffect(Unit, hoursToMs(1)) {
    viewModel.onLightGroupsRequired()
  }

  LightsWidgetView(
    hueGroupIds,
    allGroups,
    fetchError,
    { viewModel.onLightGroupsRequired() },
    { groupId, isOn -> viewModel.onToggleGroup(groupId, isOn) },
  )
}

@Composable
fun LightsWidgetView(
  groupIdsToShow: List<String>,
  allGroups: Map<String, LightGroup>?,
  fetchError: Exception?,
  onRetryClick: () -> Any,
  onToggle: (groupId: String, isOn: Boolean) -> Unit
) {
  if (fetchError != null) {
    WidgetCard {
      ErrorPanel(fetchError, onRetryClick)
    }
    return
  }
  if (allGroups == null) {
    WidgetCard {
      LoadingPanel()
    }
    return
  }

  WidgetCard {
    groupIdsToShow.forEach { groupId ->
      val group = allGroups.get(groupId) ?: return@forEach

      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Switch(
          checked = group.state.anyOn,
          onCheckedChange = { onToggle(groupId, it) }
        )
        Text(
          text = group.name,
          style = TextStyle(
            color = Color(0xFF878787),
            fontSize = 14.sp,
          )
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    LightsWidgetView(listOf("1", "2", "3"), mockLightGroups, null, { }, { _, _ -> })
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    LightsWidgetView(listOf("1", "2", "3"), null, null, { }, { _, _ -> })
  }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
  HomeMonitorTheme {
    LightsWidgetView(listOf("1", "2", "3"), null, Exception("This is an error"), { }, { _, _ -> })
  }
}
