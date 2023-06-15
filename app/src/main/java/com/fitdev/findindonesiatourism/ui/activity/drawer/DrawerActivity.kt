package com.fitdev.findindonesiatourism.ui.activity.drawer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import coil.load
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.profile.ProfileActivity
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityDrawerBinding
import com.google.android.material.navigation.NavigationView

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDrawerBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)
        supportActionBar?.title = getString(R.string.home)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        }


        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
        setProfil()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportActionBar?.title = "Home"
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
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
            R.id.nav_logout -> {
                logoutAndExit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setProfil(){
        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("FULL_NAME", "").toString()
        val email = sharedPreferences.getString("EMAIL", "").toString()
        val image = sharedPreferences.getString("PROFILE_IMAGE", "").toString()

        val headerView: View = binding.navView.getHeaderView(0)
        val profileFullName: TextView = headerView.findViewById(R.id.profileFullName)
        val profileEmail: TextView = headerView.findViewById(R.id.profileEmail)
        val profileImage: ImageView = headerView.findViewById(R.id.profileImage)
        val imageURL = "https://aplikasijpm.online/fitproject/profileImage/$image"

        profileFullName.text = fullName
        profileEmail.text = email
        profileImage.load(imageURL)
    }

    private fun logoutAndExit() {
        val dialogView = LayoutInflater
            .from(this)
            .inflate(R.layout.logout_popup, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.title_confirm))

        val alertDialog = dialogBuilder.show()

        dialogView.findViewById<Button>(R.id.btn_logout_no).setOnClickListener {
            // Tindakan saat tombol "Tidak" ditekan
            alertDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_logout_yes).setOnClickListener{
            editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}






// UNUSED CODE
//
//    private fun viewModel() {
//        val preferences = UserInstance.getInstance(dataStore)
//
//        viewModel = ViewModelProvider(
//            this@DrawerActivity,
//            DrawerViewModel.Factory(preferences)
//        )[DrawerViewModel::class.java]
//
//
//        viewModel.getToken().observe(this) { it ->
//            if (it.isEmpty()) {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            } else {
////                viewModel.getData(it)
//            }
//        }
//    }

//    companion object {
//        const val MY_APP_PREFS = "my_app_prefs"
//        const val LOGOUT = "logout success"
//    }


//    private fun drawerToggle() {
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        drawer = findViewById(R.id.drawer_layout)
//
//        navigationView()
//
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawer,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//
//        drawer.addDrawerListener(toggle)
//        toggle.syncState()
//    }

//    private fun navigationView() {
//        val navigationView: NavigationView = findViewById(R.id.nav_view)
//        navigationView.setNavigationItemSelectedListener(this)
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, HomeFragment()).commit()
//
//        navigationView.setCheckedItem(R.id.nav_home)
//    }