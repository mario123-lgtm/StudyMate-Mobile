package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.studymate.home.HomeFragment
import com.example.studymate.profile.ProfileFragment
import com.example.studymate.tugas.TugasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0
            )

            insets
        }

        val bottomNavView =
            findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        bottomNavView.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.nav_tugas -> {
                    replaceFragment(TugasFragment())
                    true
                }

                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.action_profile -> {

                val intent =
                    Intent(
                        this,
                        ProfileDeveloperActivity::class.java
                    )

                startActivity(intent)
                true
            }

            R.id.action_about -> {

                AlertDialog.Builder(this)
                    .setTitle("Tentang StudyMate")
                    .setMessage(
                        "StudyMate adalah aplikasi pengingat tugas kuliah " +
                                "yang membantu mahasiswa mencatat tugas, " +
                                "mengatur deadline, dan memantau tugas yang telah selesai."
                    )
                    .setPositiveButton("OK", null)
                    .show()

                true
            }

            R.id.action_logout -> {

                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Keluar") { _, _ ->

                        val sharedPref =
                            getSharedPreferences(
                                "studymate_pref",
                                MODE_PRIVATE
                            )

                        sharedPref.edit()
                            .putBoolean("isLogin", false)
                            .remove("username")
                            .apply()

                        val intent =
                            Intent(
                                this,
                                LoginActivity::class.java
                            )

                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finish()
                    }
                    .show()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}