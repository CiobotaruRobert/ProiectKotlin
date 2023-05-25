package com.example.proiect

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentActivity : AppCompatActivity() {
    var commentList: MutableList<ModelComment>? = null
    var modelcomment: ModelComment? = null
    var adapterComment: AdapterComment? = null
    var recyclerView: RecyclerView? = null
    var extras: Bundle? = null
    var mydb = DatabaseHelper(this@CommentActivity)
    var id_postare = 0
    var buton_send: ImageButton? = null
    var edittext: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = intent.extras
        if (extras != null) {
            id_postare = extras!!.getInt("id_postare")
        }
        setContentView(R.layout.activity_comment)
        buton_send = findViewById<View>(R.id.send) as ImageButton
        buton_send!!.setOnClickListener {
            edittext = findViewById<View>(R.id.input_comment) as EditText
            val mesaj = edittext!!.text.toString()
            val today = Date()
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val dateToStr = inputFormat.format(today)
            val cursor2 = mydb.get_current_username(MainActivity.Companion.id_user_curent)
            cursor2!!.moveToNext()
            mydb.PostComment(
                id_postare,
                mesaj,
                dateToStr,
                cursor2.getString(0),
                MainActivity.Companion.id_user_curent
            )
            recyclerView!!.adapter = adapterComment
            cursor2.close()
            recyclerView = findViewById(R.id.recyclecomment)
            val layoutManager = LinearLayoutManager(applicationContext)
            recyclerView?.setLayoutManager(layoutManager)
            commentList = ArrayList()
            val cursor = mydb.readMessagesFromCommentsTable(id_postare)
            while (cursor!!.moveToNext()) {
                modelcomment = ModelComment(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(3),
                    cursor.getString(2)
                )
                commentList?.add(modelcomment!!)
            }
            adapterComment = AdapterComment(applicationContext, commentList!!, "1", "1")
            recyclerView?.setAdapter(adapterComment)
        }
        recyclerView = findViewById(R.id.recyclecomment)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.setLayoutManager(layoutManager)
        commentList = ArrayList()
        val cursor = mydb.readMessagesFromCommentsTable(id_postare)
        while (cursor!!.moveToNext()) {
            modelcomment = ModelComment(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(3),
                cursor.getString(2)
            )
            commentList?.add(modelcomment!!)
        }
        adapterComment = AdapterComment(applicationContext, commentList!!, "1", "1")
        recyclerView?.setAdapter(adapterComment)
    }
}