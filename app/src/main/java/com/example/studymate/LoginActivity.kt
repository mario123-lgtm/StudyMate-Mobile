package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val sharedPref =
            getSharedPreferences("studymate_pref", MODE_PRIVATE)

        val registerPref =
            getSharedPreferences(
                "studymate_register_pref",
                MODE_PRIVATE
            )

        val inputUsername =
            findViewById<EditText>(R.id.inputUsername)

        val inputPassword =
            findViewById<EditText>(R.id.inputPassword)

        val btnLogin =
            findViewById<Button>(R.id.btnLogin)

        val btnRegister =
            findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val intent =
                Intent(
                    this,
                    RegisterActivity::class.java
                )

            startActivity(intent)
        }

        btnLogin.setOnClickListener {

            val username =
                inputUsername.text.toString().trim()

            val password =
                inputPassword.text.toString().trim()

            val savedUsername =
                registerPref.getString(
                    "username",
                    ""
                ) ?: ""

            val savedPassword =
                registerPref.getString(
                    "password",
                    ""
                ) ?: ""

            val loginLama =
                username == password &&
                        username.isNotEmpty()

            val loginRegistrasi =
                username == savedUsername &&
                        password == savedPassword &&
                        username.isNotEmpty()

            if (loginLama || loginRegistrasi) {

                val editor =
                    sharedPref.edit()

                editor.putBoolean(
                    "isLogin",
                    true
                )

                editor.putString(
                    "username",
                    username
                )

                editor.apply()

                val intent =
                    Intent(
                        this,
                        MainActivity::class.java
                    )

                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()

            } else {

                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage(
                        "Username dan password tidak sesuai"
                    )
                    .setPositiveButton(
                        "OK",
                        null
                    )
                    .show()
            }
        }
    }
}