package com.example.testblinq.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.testblinq.R
import com.example.testblinq.databinding.ActivityMainBinding
import com.example.testblinq.helper.AlertUtils
import com.example.testblinq.helper.SharedPreferenceHelper

class MainActivity : AppCompatActivity() {

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs: SharedPreferenceHelper = SharedPreferenceHelper.getInstance(this)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (sharedPrefs.isAlreadyInvited) {
            activityMainBinding.tvRequestText.text =
                sharedPrefs.email + " " + "is in already invited status."
            activityMainBinding.btnRequestInvite.text = "Cancel Invitation"
            activityMainBinding.btnRequestInvite.background =
                getDrawable(R.drawable.btn_cancel_background)
        } else {
            activityMainBinding.tvRequestText.text =
                "Please request an invitation via email address"
            activityMainBinding.btnRequestInvite.text = "Request Invitation"
            activityMainBinding.btnRequestInvite.background =
                getDrawable(R.drawable.btn_inivite_background)
        }
        activityMainBinding.btnRequestInvite.setOnClickListener {
            if (activityMainBinding.btnRequestInvite.text == "Cancel Invitation") {
                AlertUtils.showSimpleAlert(
                    this@MainActivity, "Cancel Invitation!",
                    "Are you sure you want to cancel your invitation?", "Yes", "No",
                    object : AlertUtils.OnAlertButtonClickListener {
                        override fun onAlertButtonClick(isPositiveButton: Boolean) {
                            if (isPositiveButton) {
                                pleaseWait(this@MainActivity)
                                val handler = Handler()
                                handler.postDelayed({
                                    sharedPrefs.edit().clear().apply()
                                    val intent =
                                        Intent(applicationContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }, 2000)
                            }
                        }
                    })
            } else {
                startActivity(Intent(applicationContext, RequestInviteActivity::class.java))
            }
        }
    }

    fun pleaseWait(context: Context?) {
        val builder = AlertDialog.Builder(
            context!!
        )
        val customLayout = layoutInflater.inflate(R.layout.layout_please_wait, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }


}