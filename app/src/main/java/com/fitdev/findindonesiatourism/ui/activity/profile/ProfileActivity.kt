package com.fitdev.findindonesiatourism.ui.activity.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import coil.load
import com.fitdev.findindonesiatourism.remote.api.users.ApiConfig
import com.fitdev.findindonesiatourism.remote.response.update.UpdateResponse
import com.fitdev.findindonesiatourism.remote.response.update.UserUpdateItem
import com.fitdev.findindonesiatourism.ui.activity.drawer.DrawerActivity
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myDialog: Dialog

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

        binding.btnSaveChanges.setOnClickListener {
            updateProfile()
        }
    }

    private fun setProfile() {
        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("FULL_NAME", "").toString()
        val username = sharedPreferences.getString("USERNAME", "").toString()
        val email = sharedPreferences.getString("EMAIL", "").toString()
        val image = sharedPreferences.getString("PROFILE_IMAGE", "").toString()
        val number = sharedPreferences.getString("PHONE_NUMBER", "").toString()

        val profileView: View = binding.profileActivity
        val profileImage: ImageView = binding.shapeableImageView
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

    private fun updateProfile() {
        val profile = "default.png"
        val username = binding.profilesUsername.text.toString()
        val email = binding.profilesEmail.text.toString()
        val password = binding.profilesPassword.text.toString()
        val fullName = binding.inputProfilesFullName.text.toString()
        val phoneNumber = binding.profilesNumber.text.toString()

        myDialog = Dialog(this)

        showLoading(true)
        val client = ApiConfig.getApiService().update(profile, username, email, password,fullName, phoneNumber)
        client.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val userData: List<UserUpdateItem> = responseBody.userUpdated
                    if (userData.isNotEmpty()) { // Periksa apakah userData tidak null dan tidak kosong
                        val user = userData[0]
                        editor.putString("ID", user.id)
                        editor.putString("EMAIL", user.email)
                        editor.putString("USERNAME", user.username)
                        editor.putString("PASSWORD", user.password)
                        editor.putString("FULL_NAME", user.fullname)
                        editor.putString("PHONE_NUMBER", user.phoneNumber)
                        editor.putString("PROFILE_IMAGE", user.profileImage)
                        editor.apply()
                    }
                    startActivity(Intent(this@ProfileActivity, DrawerActivity::class.java))
                    Toast.makeText(
                        this@ProfileActivity,
                        responseBody.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    responseBody.message.let { Log.d("ProfileActivity", it) }
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Update profile failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ProfileActivity", "Update profile failed: " + response.message())
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun showLoading(con: Boolean){
        val dialogBinding = layoutInflater.inflate(R.layout.loading_dialog, null)
        myDialog.setContentView(dialogBinding)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if(con){
            myDialog.setCancelable(false)
            myDialog.show()
        }
        else{
            myDialog.setCancelable(true)
            myDialog.dismiss()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}