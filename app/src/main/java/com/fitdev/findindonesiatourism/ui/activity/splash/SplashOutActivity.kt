package com.fitdev.findindonesiatourism.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fitdev.findindonesiatourism.R
import com.fitdev.findindonesiatourism.databinding.ActivitySplashOutBinding
import com.fitdev.findindonesiatourism.ui.activity.main.MainActivity

class SplashOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_out)

        binding = ActivitySplashOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(this@SplashOutActivity, MainActivity::class.java))
    }
}