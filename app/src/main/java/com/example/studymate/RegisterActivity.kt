package com.example.studymate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val inputNama =
            findViewById<EditText>(R.id.inputNama)

        val inputEmail =
            findViewById<EditText>(R.id.inputEmail)

        val inputTanggalLahir =
            findViewById<EditText>(R.id.inputTanggalLahir)

        val radioGroupGender =
            findViewById<RadioGroup>(R.id.radioGroupGender)

        val inputUsername =
            findViewById<EditText>(R.id.inputUsername)

        val inputPassword =
            findViewById<EditText>(R.id.inputPassword)

        val inputConfirmPassword =
            findViewById<EditText>(R.id.inputConfirmPassword)

        val btnSelanjutnya =
            findViewById<Button>(R.id.btnSelanjutnya)

        inputTanggalLahir.setOnClickListener {

            val calendar =
                Calendar.getInstance()

            val tahun =
                calendar.get(Calendar.YEAR)

            val bulan =
                calendar.get(Calendar.MONTH)

            val hari =
                calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val tanggal =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    inputTanggalLahir.setText(tanggal)
                },
                tahun,
                bulan,
                hari
            ).show()
        }

        btnSelanjutnya.setOnClickListener {

            val nama =
                inputNama.text.toString().trim()

            val email =
                inputEmail.text.toString().trim()

            val tanggalLahir =
                inputTanggalLahir.text.toString().trim()

            val username =
                inputUsername.text.toString().trim()

            val password =
                inputPassword.text.toString().trim()

            val confirmPassword =
                inputConfirmPassword.text.toString().trim()

            val jenisKelamin =
                when (radioGroupGender.checkedRadioButtonId) {

                    R.id.radioLakiLaki ->
                        "Laki-laki"

                    R.id.radioPerempuan ->
                        "Perempuan"

                    else ->
                        ""
                }

            val sharedPref =
                getSharedPreferences(
                    "studymate_register_pref",
                    MODE_PRIVATE
                )

            sharedPref.edit()
                .putString("nama", nama)
                .putString("email", email)
                .putString("tanggal_lahir", tanggalLahir)
                .putString("jenis_kelamin", jenisKelamin)
                .putString("username", username)
                .putString("password", password)
                .putString(
                    "confirm_password",
                    confirmPassword
                )
                .apply()

            val intent =
                Intent(
                    this,
                    VerificationActivity::class.java
                )

            startActivity(intent)
        }
    }
}