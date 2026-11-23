package com.example.studymate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studymate.data.api.SupabaseApiClient
import com.example.studymate.data.model.UserModel
import kotlinx.coroutines.launch
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

            val calendar = Calendar.getInstance()

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

            if (
                nama.isEmpty() ||
                email.isEmpty() ||
                tanggalLahir.isEmpty() ||
                jenisKelamin.isEmpty() ||
                username.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Semua data harus diisi",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (password != confirmPassword) {

                Toast.makeText(
                    this,
                    "Konfirmasi password tidak sama",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val user = UserModel(
                nama = nama,
                email = email,
                password = password
            )

            lifecycleScope.launch {

                try {

                    val response =
                        SupabaseApiClient.apiService.registerUser(
                            SupabaseApiClient.API_KEY,
                            user
                        )

                    if (response.isSuccessful) {

                        val sharedPref =
                            getSharedPreferences(
                                "studymate_register_pref",
                                MODE_PRIVATE
                            )

                        sharedPref.edit()
                            .putString("nama", nama)
                            .putString("email", email)
                            .putString(
                                "tanggal_lahir",
                                tanggalLahir
                            )
                            .putString(
                                "jenis_kelamin",
                                jenisKelamin
                            )
                            .putString(
                                "username",
                                username
                            )
                            .putString(
                                "password",
                                password
                            )
                            .putString(
                                "confirm_password",
                                confirmPassword
                            )
                            .apply()

                        Toast.makeText(
                            this@RegisterActivity,
                            "Registrasi berhasil",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent =
                            Intent(
                                this@RegisterActivity,
                                VerificationActivity::class.java
                            )

                        startActivity(intent)

                    } else {

                        val errorMessage =
                            response.errorBody()?.string()
                                ?: "Tidak ada detail error"

                        android.app.AlertDialog.Builder(
                            this@RegisterActivity
                        )
                            .setTitle(
                                "Registrasi gagal ${response.code()}"
                            )
                            .setMessage(errorMessage)
                            .setPositiveButton("OK", null)
                            .show()
                    }

                } catch (e: Exception) {

                    android.app.AlertDialog.Builder(
                        this@RegisterActivity
                    )
                        .setTitle("Terjadi Kesalahan")
                        .setMessage(
                            e.message ?: "Tidak diketahui"
                        )
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }
}