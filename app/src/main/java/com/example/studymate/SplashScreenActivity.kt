package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        val imgLogo =
            findViewById<View>(R.id.imgLogo)

        val tvNama =
            findViewById<View>(R.id.tvNama)

        val tvSubtitle =
            findViewById<View>(R.id.tvSubtitle)

        val tvSlogan =
            findViewById<View>(R.id.tvSlogan)

        val tvVersion =
            findViewById<View>(R.id.tvVersion)

        // Kondisi awal animasi
        imgLogo.alpha = 0f
        imgLogo.scaleX = 0.75f
        imgLogo.scaleY = 0.75f

        tvNama.alpha = 0f
        tvNama.translationY = 30f

        tvSubtitle.alpha = 0f
        tvSubtitle.translationY = 25f

        tvSlogan.alpha = 0f
        tvSlogan.translationY = 20f

        tvVersion.alpha = 0f

        // Animasi logo
        imgLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(700)
            .start()

        // Animasi nama aplikasi
        tvNama.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(300)
            .setDuration(600)
            .start()

        // Animasi subtitle
        tvSubtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(500)
            .setDuration(600)
            .start()

        // Animasi slogan
        tvSlogan.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(700)
            .setDuration(600)
            .start()

        // Animasi version
        tvVersion.animate()
            .alpha(1f)
            .setStartDelay(900)
            .setDuration(600)
            .start()

        val sharedPref =
            getSharedPreferences(
                "studymate_pref",
                MODE_PRIVATE
            )

        lifecycleScope.launch {

            delay(2200)

            val isLogin =
                sharedPref.getBoolean(
                    "isLogin",
                    false
                )

            val intent =
                if (isLogin) {

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