package com.example.studymate.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.studymate.LoginActivity
import com.example.studymate.MainActivity
import com.example.studymate.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var btnLewati: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        viewPager =
            findViewById(R.id.viewPagerOnboarding)

        btnNext =
            findViewById(R.id.btnNext)

        btnLewati =
            findViewById(R.id.btnLewati)

        val dotIndicator =
            findViewById<DotsIndicator>(
                R.id.dotIndicator
            )

        val adapter =
            OnboardingAdapter(this)

        viewPager.adapter = adapter

        dotIndicator.attachTo(viewPager)

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(
                    position: Int
                ) {
                    super.onPageSelected(position)

                    if (position == 2) {
                        btnNext.text = "Mulai"
                    } else {
                        btnNext.text = "Lanjut"
                    }
                }
            }
        )

        btnNext.setOnClickListener {

            if (viewPager.currentItem < 2) {

                viewPager.currentItem =
                    viewPager.currentItem + 1

            } else {

                finishOnboarding()
            }
        }

        btnLewati.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {

        val sharedPref =
            getSharedPreferences(
                "studymate_pref",
                MODE_PRIVATE
            )

        sharedPref.edit()
            .putBoolean(
                "onboarding_done",
                true
            )
            .apply()

        val isLogin =
            sharedPref.getBoolean(
                "isLogin",
                false
            )

        val intent =
            if (isLogin) {

                Intent(
                    this,
                    MainActivity::class.java
                )

            } else {

                Intent(
                    this,
                    LoginActivity::class.java
                )
            }

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}