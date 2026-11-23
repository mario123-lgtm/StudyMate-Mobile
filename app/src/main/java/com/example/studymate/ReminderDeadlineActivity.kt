package com.example.studymate

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.data.api.SupabaseApiClient
import com.example.studymate.data.model.ReminderDeadlineModel
import com.example.studymate.data.model.ReminderDeadlineRequest
import com.example.studymate.data.model.TugasModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReminderDeadlineActivity : AppCompatActivity() {

    private lateinit var inputTugas:
            AutoCompleteTextView

    private lateinit var inputWaktuReminder:
            AutoCompleteTextView

    private lateinit var tvDeadline:
            TextView

    private lateinit var btnAktifkanReminder:
            Button

    private lateinit var recyclerReminder:
            RecyclerView

    private lateinit var tvEmptyReminder:
            TextView

    private lateinit var reminderAdapter:
            ReminderDeadlineAdapter

    private val tasks =
        mutableListOf<TugasModel>()

    private var selectedTask:
            TugasModel? = null

    private var editingReminderId =
        -1L

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_reminder_deadline
        )

        val toolbar =
            findViewById<Toolbar>(
                R.id.toolbarReminder
            )

        setSupportActionBar(toolbar)

        supportActionBar?.apply {

            title =
                "Pengingat Deadline"

            setDisplayHomeAsUpEnabled(
                true
            )
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // =========================
        // IZIN NOTIFIKASI
        // =========================

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                ) !=
                PackageManager.PERMISSION_GRANTED
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
            findViewById(
                R.id.inputTugas
            )

        inputWaktuReminder =
            findViewById(
                R.id.inputWaktuReminder
            )

        tvDeadline =
            findViewById(
                R.id.tvDeadline
            )

        btnAktifkanReminder =
            findViewById(
                R.id.btnAktifkanReminder
            )

        recyclerReminder =
            findViewById(
                R.id.recyclerReminder
            )

        tvEmptyReminder =
            findViewById(
                R.id.tvEmptyReminder
            )

        // =========================
        // RECYCLER VIEW
        // =========================

        recyclerReminder.layoutManager =
            LinearLayoutManager(this)

        reminderAdapter =
            ReminderDeadlineAdapter(
                mutableListOf()
            ) { reminder ->

                isiFormEdit(reminder)
            }

        recyclerReminder.adapter =
            reminderAdapter

        // =========================
        // PILIHAN WAKTU
        // =========================

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

        inputWaktuReminder
            .setOnClickListener {

                inputWaktuReminder
                    .showDropDown()
            }

        // =========================
        // DELETE DENGAN SWIPE
        // =========================

        val swipeCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or
                            ItemTouchHelper.RIGHT
                ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder:
                    RecyclerView.ViewHolder,
                    target:
                    RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(
                    viewHolder:
                    RecyclerView.ViewHolder,
                    direction: Int
                ) {

                    val position =
                        viewHolder
                            .bindingAdapterPosition

                    if (
                        position !=
                        RecyclerView.NO_POSITION
                    ) {

                        val reminder =
                            reminderAdapter
                                .reminders[position]

                        hapusReminder(
                            reminder,
                            position
                        )
                    }
                }
            }

        ItemTouchHelper(
            swipeCallback
        ).attachToRecyclerView(
            recyclerReminder
        )

        // =========================
        // LOAD DATA
        // =========================

        loadData()

        // =========================
        // CREATE / UPDATE
        // =========================

        btnAktifkanReminder
            .setOnClickListener {

                simpanReminder()
            }
    }

    // =========================
    // LOAD TUGAS + REMINDER
    // =========================

    private fun loadData() {

        lifecycleScope.launch {

            try {

                val tugasResponse =
                    SupabaseApiClient
                        .apiService
                        .getTugas(
                            SupabaseApiClient.API_KEY
                        )

                if (
                    tugasResponse.isSuccessful
                ) {

                    tasks.clear()

                    tasks.addAll(
                        tugasResponse.body()
                            ?: emptyList()
                    )

                    val namaTugas =
                        tasks.map {
                            it.judul
                        }

                    val adapterTugas =
                        ArrayAdapter(
                            this@ReminderDeadlineActivity,
                            android.R.layout
                                .simple_dropdown_item_1line,
                            namaTugas
                        )

                    inputTugas.setAdapter(
                        adapterTugas
                    )

                    inputTugas
                        .setOnClickListener {

                            inputTugas
                                .showDropDown()
                        }

                    inputTugas
                        .setOnItemClickListener {
                                _,
                                _,
                                position,
                                _ ->

                            selectedTask =
                                tasks[position]

                            tvDeadline.text =
                                "Deadline: " +
                                        tasks[position]
                                            .deadline
                        }
                }

                loadReminder()

            } catch (e: Exception) {

                Toast.makeText(
                    this@ReminderDeadlineActivity,
                    "Gagal memuat data: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // READ REMINDER
    // =========================

    private fun loadReminder() {

        lifecycleScope.launch {

            try {

                val response =
                    SupabaseApiClient
                        .apiService
                        .getReminder(
                            SupabaseApiClient.API_KEY
                        )

                if (response.isSuccessful) {

                    val data =
                        response.body()
                            ?: emptyList()

                    reminderAdapter
                        .reminders
                        .clear()

                    reminderAdapter
                        .reminders
                        .addAll(data)

                    reminderAdapter
                        .notifyDataSetChanged()

                    tvEmptyReminder.visibility =
                        if (data.isEmpty()) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                } else {

                    tampilkanError(
                        "Gagal memuat pengingat",
                        response.errorBody()
                            ?.string()
                            ?: "Tidak ada detail error"
                    )
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ReminderDeadlineActivity,
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // CREATE / UPDATE
    // =========================

    private fun simpanReminder() {

        val task =
            selectedTask

        if (task == null) {

            Toast.makeText(
                this,
                "Pilih tugas terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val pilihan =
            inputWaktuReminder
                .text
                .toString()
                .trim()

        if (pilihan.isEmpty()) {

            Toast.makeText(
                this,
                "Pilih waktu pengingat",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val waktuReminder =
            hitungWaktuReminder(
                task,
                pilihan
            )

        if (waktuReminder == null) {

            Toast.makeText(
                this,
                "Format deadline tidak valid",
                Toast.LENGTH_LONG
            ).show()

            return
        }

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

        val date =
            Date(waktuReminder)

        val tanggal =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            ).format(date)

        val jam =
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.US
            ).format(date)

        val request =
            ReminderDeadlineRequest(
                judul = task.judul,
                tanggal = tanggal,
                jam = jam,
                keterangan = pilihan,
                status = "Aktif"
            )

        lifecycleScope.launch {

            try {

                // =====================
                // CREATE
                // =====================

                if (
                    editingReminderId ==
                    -1L
                ) {

                    val response =
                        SupabaseApiClient
                            .apiService
                            .tambahReminder(
                                SupabaseApiClient.API_KEY,
                                request
                            )

                    if (
                        response.isSuccessful
                    ) {

                        jadwalkanAlarm(
                            task,
                            waktuReminder
                        )

                        Toast.makeText(
                            this@ReminderDeadlineActivity,
                            "Pengingat berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()

                        resetForm()

                        loadReminder()

                    } else {

                        tampilkanError(
                            "Tambah pengingat gagal",
                            response.errorBody()
                                ?.string()
                                ?: "Tidak ada detail error"
                        )
                    }

                } else {

                    // =====================
                    // UPDATE
                    // =====================

                    val response =
                        SupabaseApiClient
                            .apiService
                            .updateReminder(
                                SupabaseApiClient.API_KEY,
                                "eq.$editingReminderId",
                                request
                            )

                    if (
                        response.isSuccessful
                    ) {

                        jadwalkanAlarm(
                            task,
                            waktuReminder
                        )

                        Toast.makeText(
                            this@ReminderDeadlineActivity,
                            "Pengingat berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()

                        resetForm()

                        loadReminder()

                    } else {

                        tampilkanError(
                            "Update pengingat gagal",
                            response.errorBody()
                                ?.string()
                                ?: "Tidak ada detail error"
                        )
                    }
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ReminderDeadlineActivity,
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // HITUNG WAKTU REMINDER
    // =========================

    private fun hitungWaktuReminder(
        task: TugasModel,
        pilihan: String
    ): Long? {

        if (
            pilihan ==
            "Tes 1 menit"
        ) {

            return System
                .currentTimeMillis() +
                    60_000
        }

        return try {

            val format =
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.US
                )

            val deadline =
                format.parse(
                    "${task.deadline} 23:59"
                ) ?: return null

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
                        return null
                }

            deadline.time -
                    pengurangan

        } catch (e: Exception) {

            null
        }
    }

    // =========================
    // ALARM MANAGER
    // =========================

    private fun jadwalkanAlarm(
        task: TugasModel,
        waktuReminder: Long
    ) {

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
            ("REMINDER_" + task.judul)
                .hashCode()

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

        alarmManager
            .setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                waktuReminder,
                pendingIntent
            )
    }

    // =========================
    // EDIT
    // =========================

    private fun isiFormEdit(
        reminder:
        ReminderDeadlineModel
    ) {

        editingReminderId =
            reminder.id

        inputTugas.setText(
            reminder.judul,
            false
        )

        selectedTask =
            tasks.firstOrNull {
                it.judul ==
                        reminder.judul
            }

        if (
            selectedTask != null
        ) {

            tvDeadline.text =
                "Deadline: " +
                        selectedTask!!
                            .deadline

        } else {

            tvDeadline.text =
                "Deadline: ${reminder.tanggal}"
        }

        inputWaktuReminder.setText(
            reminder.keterangan,
            false
        )

        btnAktifkanReminder.text =
            "Update Pengingat"
    }

    // =========================
    // DELETE
    // =========================

    private fun hapusReminder(
        reminder:
        ReminderDeadlineModel,
        position: Int
    ) {

        lifecycleScope.launch {

            try {

                val response =
                    SupabaseApiClient
                        .apiService
                        .hapusReminder(
                            SupabaseApiClient.API_KEY,
                            "eq.${reminder.id}"
                        )

                if (
                    response.isSuccessful
                ) {

                    batalkanAlarm(
                        reminder.judul
                    )

                    reminderAdapter
                        .reminders
                        .removeAt(position)

                    reminderAdapter
                        .notifyItemRemoved(
                            position
                        )

                    tvEmptyReminder.visibility =
                        if (
                            reminderAdapter
                                .reminders
                                .isEmpty()
                        ) {

                            View.VISIBLE

                        } else {

                            View.GONE
                        }

                    Snackbar.make(
                        recyclerReminder,
                        "Pengingat berhasil dihapus",
                        Snackbar.LENGTH_LONG
                    ).show()

                } else {

                    reminderAdapter
                        .notifyItemChanged(
                            position
                        )

                    tampilkanError(
                        "Hapus pengingat gagal",
                        response.errorBody()
                            ?.string()
                            ?: "Tidak ada detail error"
                    )
                }

            } catch (e: Exception) {

                reminderAdapter
                    .notifyItemChanged(
                        position
                    )

                Toast.makeText(
                    this@ReminderDeadlineActivity,
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // CANCEL ALARM
    // =========================

    private fun batalkanAlarm(
        judul: String
    ) {

        val intent =
            Intent(
                this,
                ReminderReceiver::class.java
            )

        val requestCode =
            ("REMINDER_" + judul)
                .hashCode()

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

        alarmManager.cancel(
            pendingIntent
        )

        pendingIntent.cancel()
    }

    // =========================
    // RESET FORM
    // =========================

    private fun resetForm() {

        editingReminderId =
            -1L

        selectedTask =
            null

        inputTugas.setText(
            "",
            false
        )

        inputWaktuReminder.setText(
            "",
            false
        )

        tvDeadline.text =
            "Deadline: -"

        btnAktifkanReminder.text =
            "Aktifkan Pengingat"
    }

    // =========================
    // DIALOG ERROR
    // =========================

    private fun tampilkanError(
        title: String,
        message: String
    ) {

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "OK",
                null
            )
            .show()
    }
}