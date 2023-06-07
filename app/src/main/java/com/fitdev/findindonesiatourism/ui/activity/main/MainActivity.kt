package com.fitdev.findindonesiatourism.ui.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ActivityMainBinding
import com.fitdev.findindonesiatourism.ui.activity.login.LoginActivity
import com.fitdev.findindonesiatourism.ui.activity.register.RegisterActivity
import com.fitdev.findindonesiatourism.ui.dataimg.ImageAdapter
import com.fitdev.findindonesiatourism.ui.dataimg.ImageData

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ImageAdapter
    private lateinit var dots: ArrayList<TextView>

    private val list = ArrayList<ImageData>()
    private val slideHandler = Handler()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        imageData()
        onClick()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
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
        binding.vpImg.adapter = adapter

        dots = ArrayList()
        setIndicatorDots()

        binding.vpImg.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectDot(position)
                super.onPageSelected(position)

                slideHandler.removeCallbacks(slideRun)
                slideHandler.postDelayed(slideRun, 3000)
            }
        })
    }

    private val slideRun = Runnable {
        val currentItem = binding.vpImg.currentItem
        val totalItems = binding.vpImg.adapter?.itemCount ?: 0

        if (currentItem < totalItems - 1) {
            binding.vpImg.currentItem = currentItem + 1
        } else {
            // Kembali ke awal
            binding.vpImg.currentItem = 0
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setIndicatorDots() {
        for (i in 0 until list.size) {
            dots.add(TextView(this))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                dots[i].text = Html.fromHtml("&#9679")
            }

            dots[i].textSize = 15f
            binding.dotsIndicator.addView(dots[i])
        }
    }

    private fun onClick() {
        binding.button.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.button2.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}