package com.fitdev.findindonesiatourism.ui.activity.login

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.fitdev.findindonesiatourism.R
import com.fitdev.findindonesiatourism.databinding.ActivityLoginBinding
import com.fitdev.findindonesiatourism.ui.costume.ButtonLogin
import com.fitdev.findindonesiatourism.ui.costume.EditTextEmail
import com.fitdev.findindonesiatourism.ui.costume.EditTextPassword

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: EditTextEmail
    private lateinit var password: EditTextPassword
    private lateinit var loginButton: ButtonLogin

    private var isEmail: Boolean = false
    private var isPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.EditTextEmail
        password = binding.EditTextPassword
        loginButton = binding.loginButton

        setupView()

        if (!intent.getStringExtra(EMAIL).isNullOrEmpty()) {
            email.setText(intent.getStringExtra(EMAIL))
            isEmail = true
        }
        if (!intent.getStringExtra(PASSWORD).isNullOrEmpty()) {
            password.setText(intent.getStringExtra(PASSWORD))
            isPassword = true
        }
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

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
        val passwordRegex: Regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}\$")
    }
}