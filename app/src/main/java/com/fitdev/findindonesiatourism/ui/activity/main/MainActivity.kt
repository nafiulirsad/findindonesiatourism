package com.fitdev.findindonesiatourism.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.register.RegisterActivity
import com.fitdev.findindonesiatourism.ui.dataimg.ImageAdapter
import com.fitdev.findindonesiatourism.ui.dataimg.ImageData
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ImageAdapter
    private lateinit var dots: ArrayList<TextView>

    private val list = ArrayList<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        imageData()
        onClick()
    }

    private fun imageData() {
        list.add(
            ImageData(
                imgUrl = "https://berita.99.co/wp-content/uploads/2022/03/gunung-tertinggi-di-indonesia.jpg"
            )
        )
        list.add(
            ImageData(
                imgUrl = "https://dagodreampark.co.id/media/k2/items/cache/be4e4fd1bcb87d92f342f6e3e3e1d9e2_XL.jpg"
            )
        )
        list.add(
            ImageData(
                imgUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/17/97/b7/6f/tumpak-sewu-waterfalls.jpg?w=1200&h=-1&s=1"
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