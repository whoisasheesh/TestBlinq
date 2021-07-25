package com.example.testblinq.helper

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.testblinq.R
import com.google.android.material.snackbar.Snackbar

object AlertUtils {
    fun showSnack(context: Context?, view: View?, message: String?) {
        val snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        val tv = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
        tv.setTextColor(Color.WHITE)
        tv.gravity = Gravity.CENTER
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.colorAccent
            )
        )
        snackbar.show()
    }


    fun showSimpleAlert(
        context: Context?, title: String?, message: String?, positiveText: String?,
        negativeText: String?, listener: OnAlertButtonClickListener?
    ) {
        val builder = AlertDialog.Builder(
            context!!
        )
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setMessage(Html.fromHtml(message))
        if (!TextUtils.isEmpty(positiveText)) {
            builder.setPositiveButton(positiveText) { dialogInterface, i ->
                listener?.onAlertButtonClick(
                    true
                )
            }
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder.setNegativeButton(negativeText) { dialogInterface, i ->
                listener?.onAlertButtonClick(
                    false
                )
            }
        }
        val dialog: Dialog = builder.create()
        try {
            val decorView = dialog.window!!.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        } catch (e: Exception) {
            Logger.e("showAlertMessage noStatus", e.message + " ")
        }
        dialog.show()
    }

    interface OnAlertButtonClickListener {
        fun onAlertButtonClick(isPositiveButton: Boolean)
    }
}