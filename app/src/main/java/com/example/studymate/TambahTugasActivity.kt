package com.example.studymate

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studymate.data.api.SupabaseApiClient
import com.example.studymate.data.model.TugasRequest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class TambahTugasActivity : AppCompatActivity() {

    private var tugasId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tambah_tugas)

        val inputJudul =
            findViewById<EditText>(R.id.inputJudulTugas)

        val inputMataKuliah =
            findViewById<AutoCompleteTextView>(
                R.id.inputMataKuliah
            )

        val inputDeadline =
            findViewById<EditText>(R.id.inputDeadline)

        val radioPrioritas =
            findViewById<RadioGroup>(R.id.radioPrioritas)

        val btnSimpan =
            findViewById<Button>(R.id.btnSimpanTugas)

        // =========================
        // DROPDOWN MATA KULIAH
        // =========================

        val daftarMataKuliah = arrayOf(
            "Pemrograman Mobile",
            "Big Data",
            "Probabilitas & Statistika",
            "Metodologi Penelitian",
            "Jaringan Multimedia",
            "KPPL",
            "Sistem Informasi Geografis",
            "Pengolahan Citra Digital"
        )

        val adapterMataKuliah =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                daftarMataKuliah
            )

        inputMataKuliah.setAdapter(adapterMataKuliah)

        inputMataKuliah.setOnClickListener {
            inputMataKuliah.showDropDown()
        }

        // =========================
        // DATE PICKER
        // =========================

        inputDeadline.setOnClickListener {

            val calendar =
                Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, day ->

                    // Format yang sesuai PostgreSQL/Supabase DATE:
                    // YYYY-MM-DD

                    val tanggal =
                        String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            day
                        )

                    inputDeadline.setText(tanggal)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // =========================
        // CEK MODE EDIT
        // =========================

        tugasId =
            intent.getLongExtra(
                "id",
                -1L
            )

        if (tugasId != -1L) {

            val judul =
                intent.getStringExtra("judul") ?: ""

            val mataKuliah =
                intent.getStringExtra("mataKuliah") ?: ""

            val deadline =
                intent.getStringExtra("deadline") ?: ""

            val prioritas =
                intent.getStringExtra("prioritas") ?: ""

            inputJudul.setText(judul)

            inputMataKuliah.setText(
                mataKuliah,
                false
            )

            inputDeadline.setText(deadline)

            when (prioritas) {

                "Tinggi" ->
                    radioPrioritas.check(
                        R.id.radioTinggi
                    )

                "Sedang" ->
                    radioPrioritas.check(
                        R.id.radioSedang
                    )

                "Rendah" ->
                    radioPrioritas.check(
                        R.id.radioRendah
                    )
            }

            btnSimpan.text =
                "Update Tugas"
        }

        // =========================
        // SIMPAN / UPDATE
        // =========================

        btnSimpan.setOnClickListener {

            val judul =
                inputJudul.text.toString().trim()

            val mataKuliah =
                inputMataKuliah.text.toString().trim()

            val deadline =
                inputDeadline.text.toString().trim()

            val prioritas =
                when (radioPrioritas.checkedRadioButtonId) {

                    R.id.radioTinggi ->
                        "Tinggi"

                    R.id.radioSedang ->
                        "Sedang"

                    R.id.radioRendah ->
                        "Rendah"

                    else ->
                        ""
                }

            if (
                judul.isEmpty() ||
                mataKuliah.isEmpty() ||
                deadline.isEmpty() ||
                prioritas.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Lengkapi semua data tugas",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val tugas =
                TugasRequest(
                    judul = judul,
                    deadline = deadline,
                    prioritas = prioritas,
                    kategori = mataKuliah
                )

            lifecycleScope.launch {

                try {

                    // =====================
                    // UPDATE
                    // =====================

                    if (tugasId != -1L) {

                        val response =
                            SupabaseApiClient
                                .apiService
                                .updateTugas(
                                    SupabaseApiClient.API_KEY,
                                    "eq.$tugasId",
                                    tugas
                                )

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@TambahTugasActivity,
                                "Tugas berhasil diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()

                        } else {

                            val error =
                                response.errorBody()
                                    ?.string()
                                    ?: "Tidak ada detail error"

                            Toast.makeText(
                                this@TambahTugasActivity,
                                "Update gagal: $error",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        // =====================
                        // CREATE
                        // =====================

                        val response =
                            SupabaseApiClient
                                .apiService
                                .tambahTugas(
                                    SupabaseApiClient.API_KEY,
                                    tugas
                                )

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@TambahTugasActivity,
                                "Tugas berhasil ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()

                        } else {

                            val error =
                                response.errorBody()?.string()
                                    ?: "Tidak ada detail error"

                            android.app.AlertDialog.Builder(
                                this@TambahTugasActivity
                            )
                                .setTitle(
                                    "Tambah tugas gagal ${response.code()}"
                                )
                                .setMessage(error)
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@TambahTugasActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}