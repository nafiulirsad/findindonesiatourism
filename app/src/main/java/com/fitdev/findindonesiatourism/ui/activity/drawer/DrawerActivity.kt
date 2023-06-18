package com.fitdev.findindonesiatourism.ui.activity.drawer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import coil.load
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.profile.ProfileActivity
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.DetailsFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityDrawerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView


class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDrawerBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val permissionCode = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLocation: String = "-7.293677, 112.739013"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)
        supportActionBar?.title = getString(R.string.home)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){
            val homeFragment = HomeFragment()
            homeFragment.arguments = Bundle().apply {
                putString(HomeFragment.ARG_MY_LOCATION, myLocation)
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment).commit()
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
                binding.navView.setCheckedItem(R.id.nav_home)
                val homeFragment = HomeFragment()
                homeFragment.arguments = Bundle().apply {
                    putString(HomeFragment.ARG_MY_LOCATION, "-7.293677, 112.739013")
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, homeFragment).commit()
                supportFragmentManager.beginTransaction().remove(ExploreFragment()).commit()
                supportFragmentManager.beginTransaction().remove(CategoryFragment()).commit()
                supportFragmentManager.beginTransaction().remove(FavoriteFragment()).commit()
                supportFragmentManager.beginTransaction().remove(DetailsFragment()).commit()
            }
            R.id.nav_explore -> {
                binding.navView.setCheckedItem(R.id.nav_explore)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ExploreFragment()).commit()
                supportFragmentManager.beginTransaction().remove(HomeFragment()).commit()
                supportFragmentManager.beginTransaction().remove(CategoryFragment()).commit()
                supportFragmentManager.beginTransaction().remove(FavoriteFragment()).commit()
                supportFragmentManager.beginTransaction().remove(DetailsFragment()).commit()
            }
            R.id.nav_category -> {
                binding.navView.setCheckedItem(R.id.nav_category)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CategoryFragment()).commit()
                supportFragmentManager.beginTransaction().remove(ExploreFragment()).commit()
                supportFragmentManager.beginTransaction().remove(HomeFragment()).commit()
                supportFragmentManager.beginTransaction().remove(FavoriteFragment()).commit()
                supportFragmentManager.beginTransaction().remove(DetailsFragment()).commit()
            }
            R.id.nav_favorite -> {
                binding.navView.setCheckedItem(R.id.nav_favorite)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavoriteFragment()).commit()
                supportFragmentManager.beginTransaction().remove(ExploreFragment()).commit()
                supportFragmentManager.beginTransaction().remove(CategoryFragment()).commit()
                supportFragmentManager.beginTransaction().remove(HomeFragment()).commit()
                supportFragmentManager.beginTransaction().remove(DetailsFragment()).commit()
            }
            R.id.nav_profile -> {
                binding.navView.setCheckedItem(R.id.nav_profile)
                startActivity(Intent(this@DrawerActivity, ProfileActivity::class.java))
            }
            R.id.nav_logout -> {
                logoutAndExit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return false
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

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                myLocation = "${location.latitude}, ${location.longitude}"
            }
            .addOnFailureListener {
                myLocation = "-7.282501, 112.794629"
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT).show()
                }
            }
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