package com.fitdev.findindonesiatourism.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fitdev.findindonesiatourism.remote.UserInstance
import com.fitdev.findindonesiatourism.ui.activity.drawer.DrawerActivity
import com.fitdev.findindonesiatourism.ui.activity.drawer.DrawerViewModel
import com.fitdev.findindonesiatourism.ui.activity.main.MainActivity
import com.fitdev.myapplication.databinding.ActivitySplashOutBinding

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashOutActivity : AppCompatActivity() {

    private lateinit var viewModel: DrawerViewModel

    private lateinit var binding: ActivitySplashOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel()
    }

    private fun viewModel() {
        val preferences = UserInstance.getInstance(dataStore)

        viewModel = ViewModelProvider(
            this,
            DrawerViewModel.Factory(preferences)
        )[DrawerViewModel::class.java]

        viewModel.getToken().observe(this) {
            if (it.isNullOrEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                }, 500)
            } else {
                startActivity(Intent(this, DrawerActivity::class.java))
            }
        }
    }
}