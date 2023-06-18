package com.fitdev.findindonesiatourism.ui.activity.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.fitdev.findindonesiatourism.ui.activity.drawer.DrawerActivity
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.register.RegisterActivity
import com.fitdev.findindonesiatourism.ui.dataimg.ImageAdapter
import com.fitdev.findindonesiatourism.ui.dataimg.ImageData
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ImageAdapter
    private lateinit var dots: ArrayList<TextView>

    private val list = ArrayList<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sessionCheck()
        setSlider()
        onClick()
    }

    private fun sessionCheck(){
        sharedPreferences = getSharedPreferences("MY_SESS", Context.MODE_PRIVATE)

        val getFullName = sharedPreferences.getString("FULL_NAME", "")
        val getEmail = sharedPreferences.getString("EMAIL", "")
        if(getFullName !== "" && getEmail !== ""){
            val i = Intent(this, DrawerActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun setSlider() {
        list.add(
            ImageData(
                imgUrl = "https://aplikasijpm.online/fitproject/slider/image1-min.jpg"
            )
        )
        list.add(
            ImageData(
                imgUrl = "https://aplikasijpm.online/fitproject/slider/image2-min.jpg"
            )
        )
        list.add(
            ImageData(
                imgUrl = "https://aplikasijpm.online/fitproject/slider/image3-min.jpg"
            )
        )

        adapter = ImageAdapter(list)
        binding.mainSlider.adapter = adapter

        dots = ArrayList()
        setIndicatorDots()

        @Suppress("DEPRECATION")
        binding.mainSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectDot(position)
                super.onPageSelected(position)
                Handler().removeCallbacks(slideRun)
                Handler().postDelayed(slideRun, 3000)
            }
        })
    }

    private val slideRun = Runnable {
        val currentItem = binding.mainSlider.currentItem
        val totalItems = binding.mainSlider.adapter?.itemCount ?: 0

        if (currentItem < totalItems - 1) {
            binding.mainSlider.currentItem = currentItem + 1
        } else {
            // Kembali ke awal
            binding.mainSlider.currentItem = 0
        }
    }

    private fun selectDot(position: Int) {
        for (i in 0 until list.size) {
            if (i == position) {
                dots[i].setTextColor(ContextCompat.getColor(this, R.color.Secondary))
            } else {
                dots[i].setTextColor(ContextCompat.getColor(this, R.color.Primary))
            }
        }
    }

    private fun setIndicatorDots() {
        for (i in 0 until list.size) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()

            dots[i].textSize = 15f
            binding.mainSliderDots.addView(dots[i])
        }
    }

    private fun onClick() {
        binding.mainBtnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.mainBtnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}