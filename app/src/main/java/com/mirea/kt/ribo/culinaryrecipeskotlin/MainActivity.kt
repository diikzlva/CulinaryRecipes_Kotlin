package com.mirea.kt.ribo.culinaryrecipeskotlin

import android.content.Intent
import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.apply {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        username = findViewById(R.id.username)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            if (it.id == R.id.loginButton) {
                Toast.makeText(this@MainActivity, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, RecipeActivity::class.java))
            }
        }
    }
}
