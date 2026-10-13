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

        val inputUsername =
            findViewById<EditText>(R.id.inputUsername)

        val inputPassword =
            findViewById<EditText>(R.id.inputPassword)

        val btnLogin =
            findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username =
                inputUsername.text.toString().trim()

            val password =
                inputPassword.text.toString().trim()

            if (username == password && username.isNotEmpty()) {

                val editor = sharedPref.edit()

                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                editor.apply()

                val intent =
                    Intent(this, MainActivity::class.java)

                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()

            } else {

                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username dan password tidak sesuai")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}