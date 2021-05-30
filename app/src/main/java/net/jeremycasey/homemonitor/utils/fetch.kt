import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class VolleySingleton constructor(context: Context) {
  companion object {
    @Volatile
    private var INSTANCE: VolleySingleton? = null
    fun getInstance(context: Context) =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: VolleySingleton(context).also {
          INSTANCE = it
        }
      }
  }

  val requestQueue: RequestQueue by lazy {
    // applicationContext is key, it keeps you from leaking the
    // Activity or BroadcastReceiver if someone passes one in.
    Volley.newRequestQueue(context.applicationContext)
  }
  fun <T> addToRequestQueue(req: Request<T>) {
    requestQueue.add(req)
  }
}

// Taken from: https://developer.android.com/training/volley/request-custom
class GsonRequest<ResponseType>(
  url: String,
  private val clazz: Class<ResponseType>,
  private val headers: MutableMap<String, String>?,
  private val listener: Response.Listener<ResponseType>,
  errorListener: Response.ErrorListener
) : Request<ResponseType>(Method.GET, url, errorListener) {
  private val gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()

  override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

  override fun deliverResponse(response: ResponseType) = listener.onResponse(response)

  override fun parseNetworkResponse(response: NetworkResponse?): Response<ResponseType> {
    return try {
      val json = String(
        response?.data ?: ByteArray(0),
        Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
      )
      Response.success(
        gson.fromJson(json, clazz),
        HttpHeaderParser.parseCacheHeaders(response)
      )
    } catch (e: UnsupportedEncodingException) {
      Response.error(ParseError(e))
    } catch (e: JsonSyntaxException) {
      Response.error(ParseError(e))
    }
  }
}
