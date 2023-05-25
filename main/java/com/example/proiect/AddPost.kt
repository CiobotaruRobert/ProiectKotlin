package com.example.proiect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddPost : AppCompatActivity() {
    var titlu_input: EditText? = null
    var mesaj_input: EditText? = null
    var add_button: MaterialButton? = null
    var uploadPictureButton: ImageView? = null
    var mydb: DatabaseHelper? = null
    var sqLiteDatabase: SQLiteDatabase? = null
    var imagine: Bitmap? = null
    var barray: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpost)
        titlu_input = findViewById(R.id.titlu)
        mesaj_input = findViewById(R.id.continut_postare)
        add_button = findViewById(R.id.add_post_button)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        uploadPictureButton = findViewById(R.id.upload_button)
        add_button?.setOnClickListener(View.OnClickListener {
            val mydb = DatabaseHelper(this@AddPost)
            if (barray != null && barray!!.size > 0) {
                mydb.addPost(
                    titlu_input?.getText().toString().trim { it <= ' ' },
                    mesaj_input?.getText().toString().trim { it <= ' ' },
                    MainActivity.Companion.id_user_curent.toString().trim { it <= ' ' },
                    barray
                )
            } else {
                Toast.makeText(
                    this@AddPost,
                    "Introduceti o poza pentru postare",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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
                    imagine?.compress(Bitmap.CompressFormat.PNG, 100, bos)
                    barray = bos.toByteArray()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}