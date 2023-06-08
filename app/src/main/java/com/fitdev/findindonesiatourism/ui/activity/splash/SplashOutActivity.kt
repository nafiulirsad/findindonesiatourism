package com.fitdev.findindonesiatourism.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.fitdev.findindonesiatourism.ui.activity.drawer.DrawerActivity
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivitySplashOutBinding

class SplashOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_out)

        binding = ActivitySplashOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, DrawerActivity::class.java)
            )
            finish()
        }, 500)
    }
}