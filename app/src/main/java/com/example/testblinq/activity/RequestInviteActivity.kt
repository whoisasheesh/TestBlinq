package com.example.testblinq.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.*
import com.example.testblinq.R
import com.example.testblinq.activity.RequestInviteActivity
import com.example.testblinq.api.Api
import com.example.testblinq.databinding.ActivityRequestInviteBinding
import com.example.testblinq.helper.*
import com.example.testblinq.helper.VolleyHelper.VolleyHelperInterface
import java.util.*

class RequestInviteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRequestInviteBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_request_invite)
        binding.btnSendInivitaion.setOnClickListener {
            var isFormValid = true
            if (TextUtils.isEmpty(binding.etName.text.toString())) {
                isFormValid = false
                binding.errName.setText(R.string.empty_error_msg)
                binding.errName.visibility = View.VISIBLE
            } else {
                binding.errName.visibility = View.GONE
            }
            if (TextUtils.isEmpty(binding.etEmailAddress.text.toString().trim { it <= ' ' })) {
                isFormValid = false
                binding.errorEmailAddress.setText(R.string.empty_error_msg)
                binding.errorEmailAddress.visibility = View.VISIBLE
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmailAddress.text.toString())
                        .matches()
                ) {
                    isFormValid = false
                    binding.errorEmailAddress.setText(R.string.enter_valid_email)
                    binding.errorEmailAddress.visibility = View.VISIBLE
                } else {
                    binding.errorEmailAddress.visibility = View.GONE
                }
            }
            if (TextUtils.isEmpty(binding.etConfirmEmail.text.toString().trim { it <= ' ' })) {
                isFormValid = false
                binding.errorConfirmEmailAddress.setText(R.string.empty_error_msg)
                binding.errorConfirmEmailAddress.visibility = View.VISIBLE
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etConfirmEmail.text.toString())
                        .matches()
                ) {
                    isFormValid = false
                    binding.errorConfirmEmailAddress.setText(R.string.enter_valid_email)
                    binding.errorConfirmEmailAddress.visibility = View.VISIBLE
                } else {
                    binding.errorEmailAddress.visibility = View.GONE
                }
            }
            if (binding.etConfirmEmail.text.toString()
                    .trim { it <= ' ' } != binding.etEmailAddress.text.toString().trim { it <= ' ' }
            ) {
                isFormValid = false
                binding.errorConfirmEmailAddress.setText(R.string.email_dont_match)
                binding.errorConfirmEmailAddress.visibility = View.VISIBLE
            } else {
                binding.errorConfirmEmailAddress.visibility = View.GONE
            }
            if (isFormValid) {
                if (NetworkUtils.isInNetwork(applicationContext)) {
                    requestInvitationTask(binding.etEmailAddress.text.toString().trim { it <= ' ' },
                        binding.etName.text.toString().trim { it <= ' ' })
                } else {
                    AlertUtils.showSnack(applicationContext, binding.btnSendInivitaion, "")
                }
            }
        }
    }

    fun requestInvitationTask(email: String, name: String) {
        pleaseWait(this@RequestInviteActivity)
        val volleyHelper: VolleyHelper = VolleyHelper.getInstance(this)
        val sharedPrefs: SharedPreferenceHelper = SharedPreferenceHelper.getInstance(this)
        val postParams = HashMap<String, String>()
        postParams["email"] = email
        postParams["name"] = name
        volleyHelper.addVolleyRequestListeners(
            Api.instance!!.authApi,
            Request.Method.POST,
            postParams,
            object : VolleyHelperInterface {
                override fun onSuccess(response: String) {
                    if (response == "Registered") {
                        sharedPrefs.edit()
                            .putBoolean(SharedPreferenceHelper.Companion.IS_ALREADY_INVITED, true)
                            .apply()
                        sharedPrefs.edit().putString(SharedPreferenceHelper.Companion.EMAIL, email)
                            .apply()
                        val builder = AlertDialog.Builder(this@RequestInviteActivity)
                        val customLayout =
                            layoutInflater.inflate(R.layout.layout_custom_alert_dialog, null)
                        val btnNext = customLayout.findViewById<Button>(R.id.btn_next)
                        builder.setView(customLayout)
                        val dialog = builder.create()
                        btnNext.setOnClickListener {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        dialog.show()
                        dialog.setCanceledOnTouchOutside(false)
                    }
                }

                override fun onError(errorResponse: String?, volleyError: VolleyError) {
                    Logger.e("RequestInvitationTask onError response", volleyError.message)
                }
            },
            "RequestInvitationTask"
        )
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