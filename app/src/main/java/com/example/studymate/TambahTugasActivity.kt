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
import java.util.Calendar

class TambahTugasActivity : AppCompatActivity() {

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

        // Isi dropdown mata kuliah
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

        inputDeadline.setOnClickListener {

            val calendar =
                Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, day ->

                    val tanggal =
                        "$day/${month + 1}/$year"

                    inputDeadline.setText(tanggal)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSimpan.setOnClickListener {

            val judul =
                inputJudul.text.toString().trim()

            val mataKuliah =
                inputMataKuliah.text.toString().trim()

            val deadline =
                inputDeadline.text.toString().trim()

            val prioritas =
                when (radioPrioritas.checkedRadioButtonId) {

                    R.id.radioTinggi -> "Tinggi"

                    R.id.radioSedang -> "Sedang"

                    R.id.radioRendah -> "Rendah"

                    else -> ""
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

            TaskStorage.addTask(
                this,
                judul,
                mataKuliah,
                deadline,
                prioritas
            )

            Toast.makeText(
                this,
                "Tugas berhasil ditambahkan",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}