package com.example.testblinq.helper

import android.util.Log

object Logger {
    fun e(tag: String?, message: String?) {
        Log.e(tag, message!!)
    }
}