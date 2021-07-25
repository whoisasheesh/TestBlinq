package com.example.testblinq.helper

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*

class VolleyHelper private constructor() {
    fun addVolleyRequestListeners(
        url: String, reqMethod: Int, postParams: HashMap<String, String>?,
        callbacks: VolleyHelperInterface?, requestTag: String
    ) {
        addRequestToQueue(url, reqMethod, postParams, callbacks, requestTag)
    }

    interface VolleyHelperInterface {
        fun onSuccess(response: String)
        fun onError(errorResponse: String?, volleyError: VolleyError)
    }

    private fun addRequestToQueue(
        url: String, reqMethod: Int, postParams: HashMap<String, String>?,
        callbacks: VolleyHelperInterface?, requestTag: String
    ) {
        Logger.e("$requestTag url", url)
        val request: StringRequest =
            object : StringRequest(reqMethod, url, Response.Listener { response ->
                Logger.e("$requestTag response", response)
                try {
                    callbacks?.onSuccess(response)
                } catch (e: Exception) {
                }
            }, Response.ErrorListener { error ->
                Logger.e("$requestTag error", error.message + "")
                try {
                    val errorObj = JSONObject(String(error.networkResponse.data))
                    Logger.e("$requestTag error res", errorObj.toString())
                    if (callbacks != null) {
                        callbacks.onError(errorObj.toString(), error)
                        return@ErrorListener
                    }
                } catch (e: Exception) {
                    Logger.e("$requestTag error ex", e.message + "")
                }
                try {
                    callbacks?.onError("", error)
                } catch (e: Exception) {
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    if (reqMethod == Method.POST && postParams != null) {
                        Logger.e("$requestTag Post Params", postParams.toString())
                        return postParams
                    }
                    return super.getParams()
                }
            }
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(
            50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        request.retryPolicy = retryPolicy
        request.tag = requestTag
        requestQueue!!.add(request)
    }

    companion object {
        var requestQueue: RequestQueue? = null

        @SuppressLint("StaticFieldLeak")
        var volleyHelper: VolleyHelper? = null
        var preferenceHelper: SharedPreferenceHelper? = null
        fun getInstance(context: Context): VolleyHelper {
            preferenceHelper = SharedPreferenceHelper.getInstance(context)
            return if (volleyHelper != null) {
                volleyHelper!!
            } else {
                requestQueue = Volley.newRequestQueue(context)
                volleyHelper = VolleyHelper()
                volleyHelper!!
            }
        }
    }
}