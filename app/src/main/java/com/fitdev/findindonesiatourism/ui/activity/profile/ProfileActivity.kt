package com.fitdev.findindonesiatourism.ui.activity.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import coil.load
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbars)
        supportActionBar?.apply {
            title = getString(R.string.profile)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_new)
        }

        setProfile()
    }

    private fun setProfile() {
        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("FULL_NAME", "").toString()
        val username = sharedPreferences.getString("USERNAME", "").toString()
        val email = sharedPreferences.getString("EMAIL", "").toString()
        val image = sharedPreferences.getString("PROFILE_IMAGE", "").toString()
        val number = sharedPreferences.getString("PHONE_NUMBER", "").toString()

        val profileView: View = binding.profileActivity
        val profileImage: ImageView = profileView.findViewById(R.id.shapeableImageView)
        val profileFullName: TextView = profileView.findViewById(R.id.profilesFullName)
        val profileUsername: TextView = profileView.findViewById(R.id.profilesUsername)
        val inputProfileFullName: TextView = profileView.findViewById(R.id.inputProfilesFullName)
        val profileEmail: TextView = profileView.findViewById(R.id.profilesEmail)
        val profileNumber: TextView = profileView.findViewById(R.id.profilesNumber)
        val imageURL = "https://aplikasijpm.online/fitproject/profileImage/$image"

        profileFullName.text = fullName
        inputProfileFullName.text = fullName
        profileUsername.text = username
        profileEmail.text = email
        profileNumber.text = number
        profileImage.load(imageURL)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}