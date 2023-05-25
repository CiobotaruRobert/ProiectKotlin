package com.example.proiect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ContactActivity : AppCompatActivity() {
    var subiect: EditText? = null
    var mesaj: EditText? = null
    var buton: MaterialButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contact)
        subiect = findViewById(R.id.subiect)
        mesaj = findViewById(R.id.mesaj)
        buton = findViewById(R.id.trimitere_mail)
        buton?.setOnClickListener(View.OnClickListener {
            val subiect_ = subiect?.getText().toString().trim { it <= ' ' }
            val mesaj_ = mesaj?.getText().toString().trim { it <= ' ' }
            val email = "robert.rob0408@gmail.com"
            if (subiect_.isEmpty()) {
                Toast.makeText(
                    this@ContactActivity,
                    "Va rugam introduceti subiectul!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mesaj_.isEmpty()) {
                Toast.makeText(
                    this@ContactActivity,
                    "Va rugam introduceti un mesaj!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (!subiect_.isEmpty() && !mesaj_.isEmpty()) {
                val mail = ("mailto: " + email
                        + "?&subject=" + Uri.encode(subiect_)
                        + "&body=" + Uri.encode(mesaj_))
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(mail)
                try {
                    startActivity(Intent.createChooser(intent, "Trimiteti e-mailul..."))
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ContactActivity,
                        "Exceptie: " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}