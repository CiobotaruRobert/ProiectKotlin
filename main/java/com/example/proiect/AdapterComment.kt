package com.example.proiect

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proiect.AdapterComment.MyHolder

class AdapterComment(
    var context: Context,
    var list: List<ModelComment>,
    var myuid: String,
    var postid: String
) : RecyclerView.Adapter<MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val uid = list[position].uid;
        val name = list[position].uname;
        val comment = list[position].comment;
        val timestamp = list[position].ptime;
        holder.name.text = name
        holder.time.text = timestamp
        holder.comment.text = comment
        try {
            val mydb = DatabaseHelper(holder.itemView.context)
            val cursor2 = mydb.get_profile_image_post(uid)
            cursor2!!.moveToNext()
            val bmp = BitmapFactory.decodeByteArray(cursor2.getBlob(0), 0, cursor2.getBlob(0).size)
            holder.imagea.setImageBitmap(bmp)
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagea: ImageView
        var name: TextView
        var comment: TextView
        var time: TextView

        init {
            imagea = itemView.findViewById(R.id.loadcomment)
            name = itemView.findViewById(R.id.commentname)
            comment = itemView.findViewById(R.id.commenttext)
            time = itemView.findViewById(R.id.commenttime)
        }
    }
}