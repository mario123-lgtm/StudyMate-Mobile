package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Home Dashboard"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Tombol Tambah Tugas
        val btnTambahTugas =
            findViewById<Button>(
                R.id.btnTambahTugas
            )

        btnTambahTugas.setOnClickListener {

            val intent =
                Intent(
                    this,
                    TambahTugasActivity::class.java
                )

            startActivity(intent)
        }

        // Tombol Pengingat Deadline
        val btnPengingatDeadline =
            findViewById<Button>(
                R.id.btnPengingatDeadline
            )

        btnPengingatDeadline.setOnClickListener {

            val intent =
                Intent(
                    this,
                    ReminderDeadlineActivity::class.java
                )

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu
    ): Boolean {

        menuInflater.inflate(
            R.menu.dashboard_menu,
            menu
        )

        return true
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {

        return when (item.itemId) {

            android.R.id.home -> {

                onBackPressedDispatcher
                    .onBackPressed()

                true
            }

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
                    .setTitle(
                        "Tentang StudyMate"
                    )
                    .setMessage(
                        "StudyMate adalah aplikasi pengingat tugas kuliah " +
                                "yang membantu mahasiswa mencatat tugas, " +
                                "mengatur deadline, dan memantau tugas yang telah selesai."
                    )
                    .setPositiveButton(
                        "OK",
                        null
                    )
                    .show()

                true
            }

            R.id.action_logout -> {

                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage(
                        "Apakah Anda yakin ingin keluar?"
                    )
                    .setNegativeButton(
                        "Batal",
                        null
                    )
                    .setPositiveButton(
                        "Keluar"
                    ) { _, _ ->

                        val sharedPref =
                            getSharedPreferences(
                                "studymate_pref",
                                MODE_PRIVATE
                            )

                        sharedPref.edit()
                            .putBoolean(
                                "isLogin",
                                false
                            )
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

            else ->
                super.onOptionsItemSelected(item)
        }
    }
}