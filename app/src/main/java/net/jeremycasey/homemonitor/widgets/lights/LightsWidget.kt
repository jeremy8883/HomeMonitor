package net.jeremycasey.homemonitor.widgets.lights

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.*
import net.jeremycasey.homemonitor.private.hueGroupIds
import net.jeremycasey.homemonitor.private.mainGroupId
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.secondsToMs
import net.jeremycasey.homemonitor.widgets.lights.api.changeGroupState
import net.jeremycasey.homemonitor.widgets.lights.api.fetchGroups
import net.jeremycasey.homemonitor.widgets.lights.api.fetchLights
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

class LightsWidgetViewModel(
  context: Context,
) : ViewModel() {
  private val _allGroups = MutableLiveData<Map<String, LightGroup>?>(null)
  val allGroups: LiveData<Map<String, LightGroup>?> = _allGroups

  private val _allLights = MutableLiveData<Map<String, Light>?>(null)
  val allLights: LiveData<Map<String, Light>?> = _allLights

  private val _context = context

  private val _groupFetchError = MutableLiveData<Exception?>(null)
  val groupFetchError: LiveData<Exception?> = _groupFetchError

  fun onLightDataRequired() {
    _groupFetchError.value = null
    fetchGroups(
      _context,
      { lightGroups: Map<String, LightGroup> ->
        _allGroups.value = lightGroups
      },
      { error: Exception ->
        _groupFetchError.value = error
      }
    )
    fetchLights(
      _context,
      { lights: Map<String, Light> ->
        _allLights.value = lights
      },
      { error: Exception ->
        println(error)
      }
    )
  }

  fun onToggleGroup(groupId: String, isOn: Boolean) {
    changeGroupState(
      groupId, LightGroupStateAction(on = isOn), _context,
      {
        onLightDataRequired()
      },
      {
        println(it)
      }
    )
  }
}

private fun getMainLightsForBackgroundEffect(
  allGroups: Map<String, LightGroup>?, allLights: Map<String, Light>?
): List<Light> {
  if (allGroups == null || allLights == null) return listOf()

  val group = allGroups.get(mainGroupId)
  if (group == null) return listOf()

  return group.lights.map { lightId ->
    val light = allLights.get(lightId)
    // This shouldn't happen
    if (light == null) return@getMainLightsForBackgroundEffect listOf()

    light
  }.filter { it.state.on }
}

@Composable
fun LightsWidget(viewModel: LightsWidgetViewModel, onMainLightGroupUpdated: (lights: List<Light>) -> Unit) {
  val allGroups by viewModel.allGroups.observeAsState()
  val allLights by viewModel.allLights.observeAsState()
  val fetchError by viewModel.groupFetchError.observeAsState()

  PollEffect(Unit, secondsToMs(10)) {
    viewModel.onLightDataRequired()
  }

  LaunchedEffect(allGroups, allLights) {
    val lights = getMainLightsForBackgroundEffect(allGroups , allLights)
    onMainLightGroupUpdated(lights)
  }

  LightsWidgetView(
    hueGroupIds,
    allGroups,
    fetchError,
    { viewModel.onLightDataRequired() },
    { groupId, isOn -> viewModel.onToggleGroup(groupId, isOn) },
  )
}

@Composable
fun ButtonSwitch(isChecked: Boolean, onCheckedChange: (isChecked: Boolean) -> Unit, text: String) {
  Column(
    Modifier
      .width(80.dp)
      .height(80.dp)
      .clickable(onClick = { onCheckedChange(!isChecked) })
      .border(1.dp, Color.Gray),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Switch(isChecked, {})
    Text(text, style = TextStyle(
      color = Color(0xFF878787),
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
    ), modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp))
  }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
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
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 80.dp)) {
      items(groupIdsToShow.size) { index ->
        val groupId = groupIdsToShow[index]
        val group = allGroups.get(groupId)!!

        ButtonSwitch(group.state.anyOn, { onToggle(groupId, it) }, group.name)
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    LightsWidgetView(listOf("1", "2", "3"), mockLightGroups, null, { }, { _, _ -> })
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    LightsWidgetView(listOf("1", "2", "3"), null, null, { }, { _, _ -> })
  }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun ErrorPreview() {
  HomeMonitorTheme {
    LightsWidgetView(listOf("1", "2", "3"), null, Exception("This is an error"), { }, { _, _ -> })
  }
}
