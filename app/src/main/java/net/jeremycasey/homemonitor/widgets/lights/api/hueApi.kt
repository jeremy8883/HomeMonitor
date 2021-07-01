package net.jeremycasey.homemonitor.widgets.lights.api

import GsonRequest
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import net.jeremycasey.homemonitor.private.*
import net.jeremycasey.homemonitor.widgets.lights.*
import org.json.JSONObject

private fun getHueUrl(path: String): String {
  return "${hueHostAddress}api/${hueUsername}${path}"
}

fun fetchLights(context: Context, listener: (lights: Map<String,Light>) -> Unit, errorListener: (ex: Exception) -> Unit) {
  val request = StringRequest(
    Request.Method.GET, getHueUrl("/lights"),
    { response ->
      // Sometimes getting: java.io.EOFException: End of input at line 1 column 8393 path $.
      // It will crash the app on the android emulator, but I haven't noticed it fail on a device.
      try {
        val gson = GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create()
        val lights = gson.fromJson(response, Map::class.java)
        val parsedLights = mutableMapOf<String, Light>()
        lights.mapValues { entry ->
          val value = entry.value as LinkedTreeMap<String, Any>
          val state = value.get("state") as LinkedTreeMap<String, Any>

          parsedLights += Pair(
            entry.key as String, Light(
              id = entry.key as String,
              modelid = value.get("modelid") as String,
              name = value.get("name") as String,
//          pointsymbol = Map<String, String>,
              state = LightState(
                alert = state.get("alert") as String,
                bri = (state.get("bri") as Double).toInt(),
                colormode = state.get("colormode") as String?,
                ct = (state.get("ct") as Double?)?.toInt(),
                effect = state.get("effect") as String?,
                hue = (state.get("hue") as Double?)?.toInt(),
                on = state.get("on") as Boolean,
                reachable = state.get("reachable") as Boolean,
                sat = (state.get("sat") as Double?)?.toInt(),
                xy = state.get("xy") as List<Double>?
              ),
              swversion = value.get("swversion") as String,
              type = value.get("type") as String
            )
          )
        }

        listener(parsedLights)
      } catch (ex: Exception) {
        errorListener(ex)
      }
    },
    errorListener
  )

  VolleySingleton.getInstance(context).addToRequestQueue(request)
}


fun fetchGroups(context: Context, listener: (lightGroups: Map<String, LightGroup>) -> Unit, errorListener: (ex: Exception) -> Unit) {
  val request = StringRequest(
    Request.Method.GET, getHueUrl("/groups"),
    { response ->
      try {
        val gson = GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create()
        val groups = gson.fromJson(response, Map::class.java)
        val parsedGroups = mutableMapOf<String, LightGroup>()
        groups.mapValues { entry ->
          val value = entry.value as LinkedTreeMap<String, Any>
          val state = value.get("state") as LinkedTreeMap<String, Any>
          val action = value.get("action") as LinkedTreeMap<String, Any>

          parsedGroups += Pair(
            entry.key as String, LightGroup(
              id = entry.key as String,
              action = LightGroupAction(
                alert = action.get("alert") as String,
                bri = (action.get("bri") as Double).toInt(),
                colormode = action.get("colormode") as String?,
                ct = (action.get("ct") as Double?)?.toInt(),
                effect = action.get("effect") as String?,
                hue = (action.get("hue") as Double?)?.toInt(),
                on = action.get("on") as Boolean,
                sat = (action.get("sat") as Double?)?.toInt(),
                xy = action.get("xy") as List<Double>?,
              ),
              `class` = value.get("class") as String,
              lights = value.get("lights") as List<String>,
              name = value.get("name") as String,
              recycle = value.get("recycle") as Boolean,
//          sensors = List<Any>,
              state = LightGroupState(
                allOn = state.get("all_on") as Boolean,
                anyOn = state.get("any_on") as Boolean,
              ),
              type = value.get("type") as String
            )
          )
        }

        listener(parsedGroups)
      } catch (ex: Exception) {
        errorListener(ex)
      }
    },
    errorListener
  )

  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

/**
 * More info http://www.burgestrand.se/hue-api/api/groups/
 * @param state {
 *   on?: boolean,
 *   hue?: number, - hue, in range 0 - 65535
 * }
 */
fun changeGroupState(id: String, state: LightGroupStateAction, context: Context, listener: () -> Unit, errorListener: Response.ErrorListener) {
  val jsonBody = JSONObject()
  if (state.hue != null) {
    jsonBody.put("hue", state.hue)
  }
  if (state.on != null) {
    jsonBody.put("on", state.on)
  }

  val request = GsonRequest(
    Request.Method.PUT,
    getHueUrl("/groups/$id/action"),
    Object::class.java,
    null,
    { response ->
      listener()
    },
    errorListener,
    jsonBody,
  )

  VolleySingleton.getInstance(context).addToRequestQueue(request)
}
