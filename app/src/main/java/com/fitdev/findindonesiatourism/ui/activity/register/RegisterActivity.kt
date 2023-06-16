package com.fitdev.findindonesiatourism.ui.activity.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitdev.findindonesiatourism.dataclass.users.registerData
import com.fitdev.findindonesiatourism.remote.api.users.ApiConfig
import com.fitdev.findindonesiatourism.remote.response.register.RegisterResponse
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.main.MainActivity
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var myDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        myDialog = Dialog(this)

        autoHideKeyboard()
        inputCheck()
        registerAction()
        goToLogin()
    }

    private fun inputCheck(){
        binding.registerInputFullname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.registerInputFullname.setBackgroundResource(R.drawable.custom_input_true)
                else binding.registerInputFullname.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.registerInputUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.registerInputUsername.setBackgroundResource(R.drawable.custom_input_true)
                else binding.registerInputUsername.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.registerInputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.registerInputEmail.setBackgroundResource(R.drawable.custom_input_true)
                else binding.registerInputEmail.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.registerInputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.registerInputPassword.setBackgroundResource(R.drawable.custom_input_true)
                else binding.registerInputPassword.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.registerInputPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.registerInputPhone.setBackgroundResource(R.drawable.custom_input_true)
                else binding.registerInputPhone.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun registerAction() {
        binding.btnRegister.setOnClickListener {
            hideKeyboard()

            val fullName = binding.registerInputFullname.text.toString()
            val userName = binding.registerInputUsername.text.toString()
            val emailAddress = binding.registerInputEmail.text.toString()
            val password = binding.registerInputPassword.text.toString()
            val phoneNumber = binding.registerInputPhone.text.toString()
            val profileImage = "default.png"

            val registerData = registerData(fullName, userName, emailAddress, password, phoneNumber, profileImage)
            validate(registerData)
            if(fullName.isNotEmpty() && userName.isNotEmpty() && emailAddress.isNotEmpty() && password.isNotEmpty() && phoneNumber.isNotEmpty()) doRegister(registerData)
        }
    }

    private fun validate(data: registerData){
        if(data.fullName.isEmpty()) {
            binding.registerInputFullname.setBackgroundResource(R.drawable.custom_input_false)
            binding.registerInputFullname.error = "Full name is required"
        }
        else binding.registerInputFullname.setBackgroundResource(R.drawable.custom_input_true)

        if(data.userName.isEmpty()) {
            binding.registerInputUsername.setBackgroundResource(R.drawable.custom_input_false)
            binding.registerInputUsername.error = "Username is required"
        }
        else binding.registerInputUsername.setBackgroundResource(R.drawable.custom_input_true)

        if(data.email.isEmpty()) {
            binding.registerInputEmail.setBackgroundResource(R.drawable.custom_input_false)
            binding.registerInputEmail.error = "Email address is required"
        }
        else binding.registerInputEmail.setBackgroundResource(R.drawable.custom_input_true)

        if(data.password.isEmpty()) {
            binding.registerInputPassword.setBackgroundResource(R.drawable.custom_input_false)
            binding.registerInputPassword.error = "Password is required"
        }
        else binding.registerInputPassword.setBackgroundResource(R.drawable.custom_input_true)

        if(data.phone.isEmpty()) {
            binding.registerInputPhone.setBackgroundResource(R.drawable.custom_input_false)
            binding.registerInputPhone.error = "Phone number is required"
        }
        else binding.registerInputPhone.setBackgroundResource(R.drawable.custom_input_true)
    }

    private fun doRegister(data: registerData) {
        showLoading(true)
        val client = ApiConfig.getApiService().register(data.fullName, data.userName, data.email, data.password, data.phone, data.profileImage)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d("Register Response: ", response.toString())
                    if (responseBody.success == true) {
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun goToLogin(){
        binding.registerHyperlinkLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun autoHideKeyboard(){
        binding.registerLayout.setOnClickListener{
            hideKeyboard()
        }
    }

    private fun hideKeyboard(){
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @SuppressLint("InflateParams")
    private fun showLoading(con: Boolean){
        val dialogBinding = layoutInflater.inflate(R.layout.loading_dialog, null)
        myDialog.setContentView(dialogBinding)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if(con){
            myDialog.setCancelable(false)
            myDialog.show()
        }
        else{
            myDialog.setCancelable(true)
            myDialog.dismiss()
        }
    }
}






// UNUSED CODE

//    private var isName: Boolean = true
//    private var isEmail: Boolean = false
//    private var isPassword: Boolean = false

//    private fun textEmail() {
//        binding.registerInputEmail.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                s?.isEmpty()
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                isEmail = !s.isNullOrEmpty() && emailRegex.matches(s.toString())
//                registerButtonEnable()
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//
//        })
//    }
//
//    private fun textPassword() {
//        binding.registerInputPassword.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                s?.isEmpty()
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                isPassword = !s.isNullOrEmpty() && passwordRegex.matches(s.toString())
//                registerButtonEnable()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        })
//    }


//    companion object {
//        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
//        val passwordRegex: Regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}\$")
//    }