package com.example.proiect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    var uploadPictureButton: ImageView? = null
    private var mAuth: FirebaseAuth? = null
    var imagine: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser != null) {
            finish()
            return
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        uploadPictureButton = findViewById(R.id.upload_button_profile_image)
        val btnRegister = findViewById<Button>(R.id.registerbutton)
        btnRegister.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val etUsername = findViewById<EditText>(R.id.username)
        val etEmail = findViewById<EditText>(R.id.email)
        val etPassword = findViewById<EditText>(R.id.password)
        val etRepeatPassword = findViewById<EditText>(R.id.repeat_password)
        val email = etEmail.text.toString()
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val repeat_password = etRepeatPassword.text.toString()
        if (username.isEmpty() || password.isEmpty() || repeat_password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            return
        }
        if (password != repeat_password) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    usern = username
                    val user = User(username, email)
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener {
                            showMainActivity()
                            setContentView(R.layout.activity_main)
                        }
                } else {
                    Toast.makeText(
                        this@RegisterActivity, "Authentication failed.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun buttonUpload(view: View?) {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(galleryIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                try {
                    imagine = MediaStore.Images.Media.getBitmap(contentResolver, data!!.data)
                    uploadPictureButton!!.setImageBitmap(imagine)
                    val bos = ByteArrayOutputStream()
                    imagine?.compress(Bitmap.CompressFormat.PNG, 50, bos)
                    barray_aux = bos.toByteArray()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        var usern: String? = null
        lateinit var barray_aux: ByteArray
    }
}