package com.fitdev.findindonesiatourism.ui.activity.drawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitdev.findindonesiatourism.remote.UserInstance
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.profile.ProfileActivity
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityDrawerBinding
import com.google.android.material.navigation.NavigationView

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDrawerBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var viewModel: DrawerViewModel

    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.home)

        viewModel()
        drawerToggle()
    }

    private fun viewModel() {
        val preferences = UserInstance.getInstance(dataStore)

        viewModel = ViewModelProvider(
            this@DrawerActivity,
            DrawerViewModel.Factory(preferences)
        )[DrawerViewModel::class.java]


        viewModel.getToken().observe(this) { it ->
            if (it.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
//                viewModel.getData(it)
            }
        }
    }

    private fun drawerToggle() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        navigationView()

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun navigationView() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()

            navigationView.setCheckedItem(R.id.nav_home)
        }, 0)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                Toast.makeText(this, R.string.homeview, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_explore -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ExploreFragment()).commit()
                Toast.makeText(this, R.string.exploreview, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_category -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CategoryFragment()).commit()
                Toast.makeText(this, R.string.categoryview, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_favorite -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavoriteFragment()).commit()
                Toast.makeText(this, R.string.favoriteview, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_profile -> {
                startActivity(Intent(this@DrawerActivity, ProfileActivity::class.java))
                Toast.makeText(this, R.string.profileview, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                Toast.makeText(this, R.string.profileview, Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                logout()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        val dialogView = LayoutInflater
            .from(this)
            .inflate(R.layout.logout_popup, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.title_confirm))

        val alertDialog = dialogBuilder.show()

//        dialogView.findViewById<Button>(R.id.btn_logout_yes).setOnClickListener {
//            // Tindakan saat tombol "Ya" ditekan
//            alertDialog.dismiss()
//
//            //Tambahkan kode untuk logout disini
//            val sharedPref = getSharedPreferences(MY_APP_PREFS, Context.MODE_PRIVATE)
//            val editor = sharedPref.edit()
//            val token = viewModel.saveToken("")
//            Intent(this, MainActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(this)
//                editor.remove(token.toString())
//                editor.apply()
//            }
//            Toast.makeText(this, LOGOUT, Toast.LENGTH_SHORT).show()
//        }

        dialogView.findViewById<Button>(R.id.btn_logout_no).setOnClickListener {
            // Tindakan saat tombol "Tidak" ditekan
            alertDialog.dismiss()
        }
    }

    companion object {
        const val MY_APP_PREFS = "my_app_prefs"
        const val LOGOUT = "logout success"
    }
}