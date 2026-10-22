package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_verification)

        val sharedPref =
            getSharedPreferences(
                "studymate_register_pref",
                MODE_PRIVATE
            )

        val nama =
            sharedPref.getString("nama", "") ?: ""

        val email =
            sharedPref.getString("email", "") ?: ""

        val tanggalLahir =
            sharedPref.getString("tanggal_lahir", "") ?: ""

        val jenisKelamin =
            sharedPref.getString("jenis_kelamin", "") ?: ""

        val username =
            sharedPref.getString("username", "") ?: ""

        val password =
            sharedPref.getString("password", "") ?: ""

        val confirmPassword =
            sharedPref.getString("confirm_password", "") ?: ""

        val tvNama =
            findViewById<TextView>(R.id.tvNama)

        val tvEmail =
            findViewById<TextView>(R.id.tvEmail)

        val tvTanggalLahir =
            findViewById<TextView>(R.id.tvTanggalLahir)

        val tvJenisKelamin =
            findViewById<TextView>(R.id.tvJenisKelamin)

        val tvUsername =
            findViewById<TextView>(R.id.tvUsername)

        val tvPassword =
            findViewById<TextView>(R.id.tvPassword)

        val tvConfirmPassword =
            findViewById<TextView>(R.id.tvConfirmPassword)

        val btnSubmit =
            findViewById<Button>(R.id.btnSubmit)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        tvNama.text = "Nama: $nama"
        tvEmail.text = "Email: $email"
        tvTanggalLahir.text = "Tanggal Lahir: $tanggalLahir"
        tvJenisKelamin.text = "Jenis Kelamin: $jenisKelamin"
        tvUsername.text = "Username: $username"
        tvPassword.text = "Password: $password"
        tvConfirmPassword.text =
            "Confirm Password: $confirmPassword"

        btnSubmit.setOnClickListener {

            if (
                nama.isEmpty() ||
                email.isEmpty() ||
                tanggalLahir.isEmpty() ||
                jenisKelamin.isEmpty() ||
                username.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()
            ) {

                AlertDialog.Builder(this)
                    .setTitle("Validasi Gagal")
                    .setMessage(
                        "Semua data registrasi harus diisi."
                    )
                    .setPositiveButton("OK", null)
                    .show()

            } else if (password != confirmPassword) {

                AlertDialog.Builder(this)
                    .setTitle("Validasi Gagal")
                    .setMessage(
                        "Password dan Confirm Password harus sama."
                    )
                    .setPositiveButton("OK", null)
                    .show()

            } else {

                sharedPref.edit()
                    .putBoolean("isRegistered", true)
                    .apply()

                val intent =
                    Intent(
                        this,
                        RegistrationSuccessActivity::class.java
                    )

                startActivity(intent)
                finish()
            }
        }

        btnKembali.setOnClickListener {

            val intent =
                Intent(
                    this,
                    RegisterActivity::class.java
                )

            startActivity(intent)
            finish()
        }
    }
}