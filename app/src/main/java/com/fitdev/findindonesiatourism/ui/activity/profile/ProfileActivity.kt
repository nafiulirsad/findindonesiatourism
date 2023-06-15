package com.fitdev.findindonesiatourism.ui.activity.profile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = getString(R.string.profile)

        setProfile()
    }

    private fun setProfile() {
        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("FULL_NAME", "").toString()
        val email = sharedPreferences.getString("EMAIL", "").toString()
        val image = sharedPreferences.getString("PROFILE_IMAGE", "").toString()
        val number = sharedPreferences.getString("PHONE_NUMBER", "").toString()

        val profileView: View = binding.profileActivity
        val profileImage: ImageView = profileView.findViewById(R.id.shapeableImageView)
        val profileFullName: TextView = profileView.findViewById(R.id.profilesFullName)
        val profileEmail: TextView = profileView.findViewById(R.id.profilesEmail)
        val profileNumber: TextView = profileView.findViewById(R.id.profilesNumber)
        val imageURL = "https://aplikasijpm.online/fitproject/profileImage/$image"

        profileFullName.text = fullName
        profileEmail.text = email
        profileNumber.text = number
        profileImage.load(imageURL)
    }
}