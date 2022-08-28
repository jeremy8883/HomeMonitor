package net.jeremycasey.homemonitor.widgets.tv

import GsonRequest
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import net.jeremycasey.homemonitor.private.*
import net.jeremycasey.homemonitor.widgets.lights.*
import org.json.JSONObject

private fun getHomeAssistantUrl(path: String): String {
  return "${homeAssistantHostAddress}api${path}"
}

/**
 * More info http://www.burgestrand.se/hue-api/api/groups/
 * @param state {
 *   on?: boolean,
 *   hue?: number, - hue, in range 0 - 65535
 * }
 */
fun toggleTv(isOn: Boolean, context: Context, listener: () -> Unit, errorListener: Response.ErrorListener) {
  val jsonBody = JSONObject()
  jsonBody.put("on", isOn)

  val headers = mutableMapOf<String, String>()
  headers += Pair("Authorization", "Bearer ${homeAssistantAccessToken}")

  val script = if (isOn) homeAssistantTvOnScriptName else homeAssistantTvOffScriptName

  val request = GsonRequest(
    Request.Method.POST,
    getHomeAssistantUrl("/services/script/${script}"),
    Object::class.java,
    headers,
    { response ->
      listener()
    },
    errorListener,
    jsonBody,
  )

  VolleySingleton.getInstance(context).addToRequestQueue(request)
}
