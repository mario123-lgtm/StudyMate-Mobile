package com.example.studymate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        val sharedPref =
            getSharedPreferences("studymate_pref", MODE_PRIVATE)

        lifecycleScope.launch {

            delay(2000)

            val isLogin =
                sharedPref.getBoolean("isLogin", false)

            val intent = if (isLogin) {

                Intent(
                    this@SplashScreenActivity,
                    MainActivity::class.java
                )

            } else {

                Intent(
                    this@SplashScreenActivity,
                    LoginActivity::class.java
                )
            }

            startActivity(intent)
            finish()
        }
    }
}