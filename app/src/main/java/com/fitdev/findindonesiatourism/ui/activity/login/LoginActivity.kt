package com.fitdev.findindonesiatourism.ui.activity.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.fitdev.findindonesiatourism.dataclass.users.loginData
import com.fitdev.findindonesiatourism.remote.api.users.ApiConfig
import com.fitdev.findindonesiatourism.remote.response.login.LoginResponse
import com.fitdev.findindonesiatourism.remote.response.login.UserLoggedInItem
import com.fitdev.findindonesiatourism.ui.activity.drawer.DrawerActivity
import com.fitdev.findindonesiatourism.ui.activity.register.RegisterActivity
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var myDialog: Dialog
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val getFullName = sharedPreferences.getString("FULL_NAME", "")
        val getEmail = sharedPreferences.getString("EMAIL", "")
        if(getFullName !== "" && getEmail !== ""){
            val i = Intent(this, DrawerActivity::class.java)
            startActivity(i)
            finish()
        }

        myDialog = Dialog(this)

        autoHideKeyboard()
        inputCheck()
        loginAction()
        goToRegister()
    }

    private fun inputCheck(){
        binding.loginInputUsernameEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.loginInputUsernameEmail.setBackgroundResource(R.drawable.custom_input_true)
                else binding.loginInputUsernameEmail.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.loginInputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) binding.loginInputPassword.setBackgroundResource(R.drawable.custom_input_true)
                else binding.loginInputPassword.setBackgroundResource(R.drawable.custom_input_false)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun loginAction() {
        binding.btnLogin.setOnClickListener {
            hideKeyboard()

            val usernameEmail = binding.loginInputUsernameEmail.text.toString()
            val password = binding.loginInputPassword.text.toString()
            val loginData = loginData(usernameEmail, password)
            validate(loginData)

            if(usernameEmail.isNotEmpty() && password.isNotEmpty()) doLogin(loginData)
        }
    }

    private fun validate(data: loginData){
        if(data.usernameEmail.isEmpty()) {
            binding.loginInputUsernameEmail.setBackgroundResource(R.drawable.custom_input_false)
            binding.loginInputUsernameEmail.error = "Username or email address is required"
        }
        else binding.loginInputUsernameEmail.setBackgroundResource(R.drawable.custom_input_true)

        if(data.password.isEmpty()) {
            binding.loginInputPassword.setBackgroundResource(R.drawable.custom_input_false)
            binding.loginInputPassword.error = "Password is required"
        }
        else binding.loginInputPassword.setBackgroundResource(R.drawable.custom_input_true)
    }

    private fun doLogin(data: loginData){
        showLoading(true)
        val client = ApiConfig.getApiService().login(data.usernameEmail, data.password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d("Login Response: ", response.toString())
                    if (responseBody.success == true) {
                        val userData: List<UserLoggedInItem> = responseBody.userLoggedIn
                        userData[0].let{
                            editor.putString("ID", it.id)
                            editor.putString("FULL_NAME", it.fullname)
                            editor.putString("USERNAME", it.username)
                            editor.putString("EMAIL", it.email)
                            editor.putString("PHONE_NUMBER", it.phoneNumber)
                            editor.putString("PROFILE_IMAGE", it.profileImage)
                            editor.apply()
                        }
                        startActivity(Intent(this@LoginActivity, DrawerActivity::class.java))
                        finish()
                        Toast.makeText(
                            this@LoginActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goToRegister() {
        binding.loginHyperlinkRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun autoHideKeyboard(){
        binding.loginLayout.setOnClickListener{
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
//companion object {
//    const val EMAIL = "email"
//    const val PASSWORD = "password"
//    val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
//    val passwordRegex: Regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}\$")
//}


//                            save(responseBody.login as Login)
//                            Toast.makeText(
//                                this@LoginActivity,
//                                "Success",
//                                Toast.LENGTH_SHORT
//                            ).show()


//    private fun loginButtonEnable() {
//        binding.btnLogin.isEnabled = isEmail && isPassword
//    }

//    private fun textEmail() {
//        binding.loginInputEmail.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                s?.isEmpty()
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                isEmail = !s.isNullOrEmpty() && emailRegex.matches(s.toString())
//                loginButtonEnable()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })
//    }
//
//    private fun textPassword() {
//        binding.loginInputPassword.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                s?.isEmpty()
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                isPassword = !s.isNullOrEmpty() && passwordRegex.matches(s.toString())
//                loginButtonEnable()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })
//    }

//        if (!intent.getStringExtra(EMAIL).isNullOrEmpty()) {
//            binding.loginInputEmail.setText(intent.getStringExtra(EMAIL))
//            isEmail = true
//        }
//        if (!intent.getStringExtra(PASSWORD).isNullOrEmpty()) {
//            binding.loginInputPassword.setText(intent.getStringExtra(PASSWORD))
//            isPassword = true
//        }


//    private var isEmail: Boolean = false
//    private var isPassword: Boolean = false


//    private fun viewModel() {
//        viewModel = ViewModelProvider(
//            this@LoginActivity,
//            LoginViewModel.Factory(preferences = UserInstance.getInstance(dataStore))
//        )[LoginViewModel::class.java]
//    }
//
//
//
//    private fun save(login: Login) {
//        viewModel.saveToken(login.id)
//        val intent = Intent(this@LoginActivity, DrawerActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
//    }