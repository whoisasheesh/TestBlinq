package com.example.testblinq.helper

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    //check for network connection
    fun isInNetwork(ctx: Context?): Boolean {
        if (ctx == null) return false
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}