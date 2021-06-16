package hani.momanii.supernova_emoji_library.gif

import android.util.Log
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author kasmadi
 * @date 14/06/21
 */
class GiftService(val url: String, var callback: Callback) {
    // make initial search request for the first 8 items
    private var link: String = url
    private var gift: ArrayList<String?> = arrayListOf()
    private var callback1: Callback? = null
    val getData: Unit
        // load the results for the user
        get() {
            object : Thread() {
                override fun run() {
                    val searchResult = searchResults
                    callback1 = callback
                    // load the results for the user
                    if (searchResult?.getJSONArray("results") != null) {
                        val gson = Gson()
                        val data = gson.fromJson(searchResult.toString(), Response::class.java)
                        callback1?.onSuccess(data.results)
                    }


                }
            }.start()
        }

    val data: ArrayList<String?>
        get() = gift


    /**
     * Get Search Result GIFs
     */
    val searchResults: JSONObject?
        get() {
            // make search request - using default locale of EN_US
            try {
                return get(link)
            } catch (ignored: IOException) {
            } catch (ignored: JSONException) {
            }
            return null
        }

    /**
     * Construct and run a GET request
     */
    @Throws(IOException::class, JSONException::class)
    private operator fun get(url: String): JSONObject {
        var connection: HttpURLConnection? = null
        try {
            // Get request
            connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            // Handle failure
            val statusCode = connection.responseCode
            Log.i("TAG", "status $statusCode")
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                val error = String.format("HTTP Code: '%1\$s' from '%2\$s'", statusCode, url)
                throw ConnectException(error)
            }

            // Parse response
            return parser(connection)
        } catch (ignored: Exception) {
            Log.e("TAG", "get: " + ignored.message)
        } finally {
            connection?.disconnect()
        }
        return JSONObject("")
    }

    /**
     * Parse the response into JSONObject
     */
    @Throws(JSONException::class)
    private fun parser(connection: HttpURLConnection?): JSONObject {
        val buffer = CharArray(1024 * 4)
        var n: Int
        var stream: InputStream? = null
        try {
            stream = BufferedInputStream(connection!!.inputStream)
            val reader = InputStreamReader(stream, "UTF-8")
            val writer = StringWriter()
            while (-1 != reader.read(buffer).also { n = it }) {
                writer.write(buffer, 0, n)
            }
            return JSONObject(writer.toString())
        } catch (ignored: IOException) {
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (ignored: IOException) {
                }
            }
        }
        return JSONObject("")
    }

    fun setUrl(url: String) {
        this.link = url
    }


    interface Callback {
        fun onSuccess(data: List<ResultsItem>?)
    }
}