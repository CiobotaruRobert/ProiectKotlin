package com.example.proiect

import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookmarkedPosts : AppCompatActivity() {
    private var recyclerView2: RecyclerView? = null
    private var arrayList2: ArrayList<Feed>? = null
    var mydb: DatabaseHelper? = null
    var id_postare: ArrayList<String>? = null
    var titlu_postare: ArrayList<String>? = null
    var mesaj_postare: ArrayList<String>? = null
    var id_postari: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        mydb = DatabaseHelper(this@BookmarkedPosts)
        id_postare = ArrayList()
        titlu_postare = ArrayList()
        mesaj_postare = ArrayList()
        arrayList2 = ArrayList()
        recyclerView2 = findViewById(R.id.recyclerView)
        var cursor1: Cursor? = null
        var cursor2: Cursor? = null
        var cursor3: Cursor? = null
        cursor1 = mydb!!.get_id_bookmarked_by_current_user(MainActivity.Companion.id_user_curent)
        while (cursor1!!.moveToNext()) {
            cursor2 = mydb!!.get_post_bookmarked_by_current_user(cursor1.getString(0))
            while (cursor2!!.moveToNext()) {
                cursor3 = mydb!!.get_imagine_profil(MainActivity.Companion.id_user_curent)
                cursor3!!.moveToNext()
                val bmpprofil =
                    BitmapFactory.decodeByteArray(cursor3.getBlob(0), 0, cursor3.getBlob(0).size)
                val bmp =
                    BitmapFactory.decodeByteArray(cursor2.getBlob(5), 0, cursor2.getBlob(5).size)
                arrayList2!!.add(Feed(bmpprofil, bmp, cursor2.getString(1), cursor2.getString(2)))
            }
            //Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
        }
        val recyclerAdapter2 = RecyclerAdapter(arrayList2!!)
        recyclerView2?.setAdapter(recyclerAdapter2)
        recyclerView2?.setLayoutManager(LinearLayoutManager(this))
    }

    companion object {
        var id_user_curent: String? = null
    }
}