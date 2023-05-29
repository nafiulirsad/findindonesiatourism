package com.fitdev.findindonesiatourism.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.fitdev.findindonesiatourism.R
import com.fitdev.findindonesiatourism.databinding.ActivitySplashBinding
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }, 3000)
    }
}