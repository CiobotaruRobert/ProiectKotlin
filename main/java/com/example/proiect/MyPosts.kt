package com.example.proiect

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyPosts : AppCompatActivity() {
    public override fun onStart() {
        super.onStart()
        active = true
    }

    public override fun onStop() {
        super.onStop()
        active = false
    }

    private var recyclerView2: RecyclerView? = null
    private var arrayList2: ArrayList<Feed>? = null
    private var poza_postare: ArrayList<Bitmap>? = null
    var mydb: DatabaseHelper? = null
    var id_postare: ArrayList<String>? = null
    var titlu_postare: ArrayList<String>? = null
    var mesaj_postare: ArrayList<String>? = null
    var chei_useri: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        mydb = DatabaseHelper(this@MyPosts)
        id_postare = ArrayList()
        titlu_postare = ArrayList()
        mesaj_postare = ArrayList()
        poza_postare = ArrayList()
        get_postarile_mele(MainActivity.Companion.id_user_curent)
        arrayList2 = ArrayList()
        recyclerView2 = findViewById(R.id.recyclerView)
        for (i in titlu_postare!!.indices) {
            val cursor3 = mydb!!.get_imagine_profil(MainActivity.Companion.id_user_curent)
            cursor3!!.moveToNext()
            val bmpprofil =
                BitmapFactory.decodeByteArray(cursor3.getBlob(0), 0, cursor3.getBlob(0).size)
            arrayList2!!.add(
                Feed(
                    bmpprofil,
                    poza_postare!![i],
                    titlu_postare!![i],
                    mesaj_postare!![i]
                )
            )
        }
        //arrayList2.add(new Feed(R.drawable.cat2,R.drawable.cat,"a","b"));
        val recyclerAdapter2 = RecyclerAdapter(arrayList2!!)
        recyclerView2?.setAdapter(recyclerAdapter2)
        recyclerView2?.setLayoutManager(LinearLayoutManager(this))
    }

    fun get_postarile_mele(aux2: String?) {
        val cursor = mydb!!.get_my_posts(aux2)
        if (cursor!!.count == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                id_postare!!.add(cursor.getString(0))
                titlu_postare!!.add(cursor.getString(1))
                mesaj_postare!!.add(cursor.getString(2))
                val bmp =
                    BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).size)
                poza_postare!!.add(bmp)
            }
        }
    }

    companion object {
        var active = false
        var id_user_curent: String? = null
    }
}