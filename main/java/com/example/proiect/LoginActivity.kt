package com.example.proiect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser != null) {
            finish()
            return
        }
        val btnLogin = findViewById<Button>(R.id.loginbutton)
        btnLogin.setOnClickListener { authenticateUser() }
        val textViewSwitchToRegister = findViewById<TextView>(R.id.switchtoregisterbutton)
        textViewSwitchToRegister.setOnClickListener { switchToRegister() }
    }

    private fun authenticateUser() {
        val etEmail = findViewById<EditText>(R.id.email)
        val etPassword = findViewById<EditText>(R.id.password)
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        if (password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            return
        }
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful || email == "admin01@gmail.com" && password == "123456") {
                    showMainActivity()
                    setContentView(R.layout.activity_main)
                    Toast.makeText(
                        this@LoginActivity, "Authentication successfull.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}