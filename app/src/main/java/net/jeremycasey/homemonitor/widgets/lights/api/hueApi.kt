package net.jeremycasey.homemonitor.widgets.lights.api

import GsonRequest
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import net.jeremycasey.homemonitor.private.*
import net.jeremycasey.homemonitor.widgets.lights.*
import java.lang.reflect.Type
import java.nio.charset.Charset

private fun getHueUrl(path: String): String {
  return "${hueHostAddress}api/${hueUsername}${path}"
}

//fun fetchLightInfo(id: String, context: Context, listener: Response.Listener<Light>, errorListener: Response.ErrorListener) {
//  val request = GsonRequest<Light>(
//    getHueUrl("/lights/$id"),
//    Light::class.java,
//    null,
//    listener,
//    errorListener
//  )
//  VolleySingleton.getInstance(context).addToRequestQueue(request)
//}
//

fun fetchGroups(context: Context, listener: (lightGroups: Map<String, LightGroup>) -> Unit, errorListener: Response.ErrorListener) {
  val request = StringRequest(
    Request.Method.GET, getHueUrl("/groups"),
    { response ->
      println("??/")
      println(response)
      val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
      val groups = gson.fromJson(response, Map::class.java)
      val parsedGroups = mutableMapOf<String, LightGroup>()
      groups.mapValues { entry ->
        val value = entry.value as LinkedTreeMap<String, Any>
        val state = value.get("state") as LinkedTreeMap<String, Any>
        val action = value.get("action") as LinkedTreeMap<String, Any>

        parsedGroups += Pair(entry.key as String, LightGroup(
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
        ))
      }
      println(parsedGroups)

      listener(parsedGroups)
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
//fun changeGroupState(id: String, state: LightGroupStateAction, context: Context, listener: (Map<String, LightGroup>) -> Unit, errorListener: Response.ErrorListener) {
//  val request = StringRequest(
//    Request.Method.GET, getHueUrl("/lights/$id/state"),
//    { response ->
//      val gson = GsonBuilder()
//        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//        .create()
//      val groups = gson.fromJson(response, Map::class.java)
//      listener(groups as Map<String, LightGroup>)
//    },
//    errorListener
//  )
//
//  VolleySingleton.getInstance(context).addToRequestQueue(request)
//
//  val request = GsonRequest<LightGroup>(
//    getHueUrl("/lights/$id/state"),
//    LightGroup::class.java, // TODO PUT, body: state: LightGroupStateAction
//    null,
//    listener,
//    errorListener
//  )
//  VolleySingleton.getInstance(context).addToRequestQueue(request)
//}
