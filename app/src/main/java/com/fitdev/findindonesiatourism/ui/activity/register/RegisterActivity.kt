package com.fitdev.findindonesiatourism.ui.activity.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.fitdev.findindonesiatourism.remote.api.ApiConfig
import com.fitdev.findindonesiatourism.remote.response.register.RegisterResponse
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity.Companion.emailRegex
import com.fitdev.findindonesiatourism.ui.activity.main.MainActivity
import com.fitdev.findindonesiatourism.ui.costume.ButtonRegister
import com.fitdev.findindonesiatourism.ui.costume.EditTextEmail
import com.fitdev.findindonesiatourism.ui.costume.EditTextPassword
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityRegisterBinding
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var email: EditTextEmail
    private lateinit var password: EditTextPassword
    private lateinit var registerButton: ButtonRegister

    private var isName: Boolean = true
    private var isEmail: Boolean = false
    private var isPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.EditTextUsername
        email = binding.EditTextEmail
        password = binding.EditTextPassword
        registerButton = binding.registerButton
        binding.EditTextNumberPhone

        setupView()
        onClicked()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun onClicked() {
        registerButtonEnable()
        textEmail()
        textPassword()

        registerButton.setOnClickListener {
            onClickCallback()
        }

        binding.textView4.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerButtonEnable() {
        registerButton.isEnabled = isName && isEmail && isPassword
    }

    private fun textEmail() {
        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.isEmpty()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEmail = !s.isNullOrEmpty() && emailRegex.matches(s.toString())
                registerButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun textPassword() {
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.isEmpty()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isPassword = !s.isNullOrEmpty() && passwordRegex.matches(s.toString())
                registerButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

//    private fun createRequestBody(): RequestBody {
//        val email = email.text.toString()
//        val username = binding.EditTextUsername.text.toString()
//        val password = password.text.toString()
//        val phoneNumber = binding.EditTextNumberPhone.text.toString()
//
////        val requestBody = RequestBody.create(
////            "application/raw".toMediaTypeOrNull(), """
////        {
////            "username": "$username",
////            "email": "$email",
////            "password": "$password",
////            "phoneNumber": "$phoneNumber"
////        }
////    """.trimIndent())
////
////        return requestBody
//
////        val rawBody = """
////        {
////            "email": "$email",
////            "username": "$username",
////            "password": "$password",
////            "phoneNumber": "$phoneNumber"
////        }
////    """.trimIndent()
////
////        return RequestBody.create("application/raw".toMediaType(), rawBody)
//
//        val requestBody = FormBody.Builder()
//            .add(EMAIL, email)
//            .add(USERNAME, username)
//            .add(PASSWORD, password)
//            .add(PHONE, phoneNumber)
//            .build()
//
//        return requestBody
//    }

    private fun onClickCallback() {
//        val requestBody = createRequestBody()
        val client = ApiConfig.getApiService().register(
            binding.EditTextUsername.text.toString(),
            email.text.toString(),
            password.text.toString(),
            binding.EditTextNumberPhone.text.toString()
        )
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Success",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        Toast.makeText(
                            this@RegisterActivity,
                            "Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val EMAIL = "email"
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val PHONE = "phoneNumber"

        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
        val passwordRegex: Regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}\$")
    }
}