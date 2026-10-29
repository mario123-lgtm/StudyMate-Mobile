package com.example.studymate

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderDeadlineActivity : AppCompatActivity() {

    private lateinit var inputTugas: AutoCompleteTextView
    private lateinit var inputWaktuReminder: AutoCompleteTextView
    private lateinit var tvDeadline: TextView

    private var selectedTask: TaskItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reminder_deadline)

        val toolbar =
            findViewById<Toolbar>(
                R.id.toolbarReminder
            )

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Pengingat Deadline"
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Android 13+ meminta izin notifikasi
        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    100
                )
            }
        }

        inputTugas =
            findViewById(R.id.inputTugas)

        inputWaktuReminder =
            findViewById(R.id.inputWaktuReminder)

        tvDeadline =
            findViewById(R.id.tvDeadline)

        val btnAktifkanReminder =
            findViewById<Button>(
                R.id.btnAktifkanReminder
            )

        val tasks =
            TaskStorage.getTasks(this)

        val namaTugas =
            tasks.map { it.judul }

        val adapterTugas =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                namaTugas
            )

        inputTugas.setAdapter(adapterTugas)

        inputTugas.setOnClickListener {
            inputTugas.showDropDown()
        }

        inputTugas.setOnItemClickListener {
                _, _, position, _ ->

            selectedTask =
                tasks[position]

            tvDeadline.text =
                "Deadline: ${tasks[position].deadline}"
        }

        val pilihanReminder =
            arrayOf(
                "Tes 1 menit",
                "10 menit sebelum",
                "30 menit sebelum",
                "1 jam sebelum",
                "1 hari sebelum"
            )

        val adapterReminder =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                pilihanReminder
            )

        inputWaktuReminder.setAdapter(
            adapterReminder
        )

        inputWaktuReminder.setOnClickListener {
            inputWaktuReminder.showDropDown()
        }

        btnAktifkanReminder.setOnClickListener {

            val task = selectedTask

            if (task == null) {

                Toast.makeText(
                    this,
                    "Pilih tugas terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val pilihan =
                inputWaktuReminder.text
                    .toString()

            if (pilihan.isEmpty()) {

                Toast.makeText(
                    this,
                    "Pilih waktu pengingat",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            jadwalkanReminder(
                task,
                pilihan
            )
        }
    }

    private fun jadwalkanReminder(
        task: TaskItem,
        pilihan: String
    ) {

        try {

            // ==============================
            // TEST NOTIFIKASI 1 MENIT
            // ==============================
            if (pilihan == "Tes 1 menit") {

                val intent =
                    Intent(
                        this,
                        ReminderReceiver::class.java
                    )

                intent.putExtra(
                    "nama_tugas",
                    task.judul
                )

                intent.putExtra(
                    "deadline",
                    task.deadline
                )

                val pendingIntent =
                    PendingIntent.getBroadcast(
                        this,
                        ("TEST_" + task.judul).hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or
                                PendingIntent.FLAG_IMMUTABLE
                    )

                val alarmManager =
                    getSystemService(
                        ALARM_SERVICE
                    ) as AlarmManager

                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 60_000,
                    pendingIntent
                )

                Toast.makeText(
                    this,
                    "Tes notifikasi akan muncul 1 menit lagi",
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            // ==============================
            // REMINDER DEADLINE SEBENARNYA
            // ==============================

            val format =
                SimpleDateFormat(
                    "d/M/yyyy HH:mm",
                    Locale.getDefault()
                )

            // Deadline sementara dianggap pukul 23:59
            val deadline =
                format.parse(
                    "${task.deadline} 23:59"
                ) ?: return

            val pengurangan =
                when (pilihan) {

                    "10 menit sebelum" ->
                        10 * 60 * 1000L

                    "30 menit sebelum" ->
                        30 * 60 * 1000L

                    "1 jam sebelum" ->
                        60 * 60 * 1000L

                    "1 hari sebelum" ->
                        24 * 60 * 60 * 1000L

                    else ->
                        0L
                }

            val waktuReminder =
                deadline.time - pengurangan

            if (
                waktuReminder <=
                System.currentTimeMillis()
            ) {

                Toast.makeText(
                    this,
                    "Waktu pengingat sudah lewat",
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            val intent =
                Intent(
                    this,
                    ReminderReceiver::class.java
                )

            intent.putExtra(
                "nama_tugas",
                task.judul
            )

            intent.putExtra(
                "deadline",
                task.deadline
            )

            val requestCode =
                task.judul.hashCode()

            val pendingIntent =
                PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            val alarmManager =
                getSystemService(
                    ALARM_SERVICE
                ) as AlarmManager

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                waktuReminder,
                pendingIntent
            )

            Toast.makeText(
                this,
                "Pengingat berhasil diaktifkan",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Format deadline tidak valid",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}