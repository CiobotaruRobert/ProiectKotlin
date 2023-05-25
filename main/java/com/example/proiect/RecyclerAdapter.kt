package com.example.proiect

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class RecyclerAdapter(private var arrayList: ArrayList<Feed>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    fun setFilteredList(filteredList: ArrayList<Feed>) {
        arrayList = filteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = arrayList[position]
        holder.title.text = feed.title
        holder.message.text = feed.message
        holder.profileImage.setImageBitmap(feed.profileIcon)
        holder.postImage.setImageBitmap(feed.postImage)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImage: ImageView
        var postImage: ImageView
        var title: TextView
        var message: TextView
        var like_button: ImageButton

        init {
            profileImage = itemView.findViewById(R.id.ivProfile)
            postImage = itemView.findViewById(R.id.ivPost)
            title = itemView.findViewById(R.id.title)
            message = itemView.findViewById(R.id.message)
            like_button = itemView.findViewById(R.id.ivLike)
            val mydb = DatabaseHelper(itemView.context)
            val like_counter = itemView.findViewById<TextView>(R.id.like_counter)
            like_counter.text = mydb.get_like_counter(adapterPosition + 1).toString()
            itemView.findViewById<View>(R.id.ivLike).setOnClickListener { view: View? ->
                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                if (currentFirebaseUser == null) Toast.makeText(
                    itemView.context, "Trebuie sa fiti autentificat pentru a aprecia postarea",
                    Toast.LENGTH_SHORT
                ).show() else {
                    val aux = adapterPosition + 1
                    if (mydb.check_if_like_unique(
                            aux,
                            MainActivity.Companion.id_user_curent
                        ) == 1
                    ) {
                        mydb.add_like_to_post(aux, MainActivity.Companion.id_user_curent)
                        Toast.makeText(
                            itemView.context,
                            "Ati apreciat postarea",
                            Toast.LENGTH_SHORT
                        ).show()
                        like_counter.text = mydb.get_like_counter(adapterPosition + 1).toString()
                    } else {
                        mydb.undo_like_to_post(aux, MainActivity.Companion.id_user_curent)
                        Toast.makeText(
                            itemView.context,
                            "Acum nu mai apreciati postarea",
                            Toast.LENGTH_SHORT
                        ).show()
                        like_counter.text = mydb.get_like_counter(adapterPosition + 1).toString()
                    }
                }
            }
            itemView.findViewById<View>(R.id.ivComment).setOnClickListener { view: View? ->
                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                if (currentFirebaseUser == null) Toast.makeText(
                    itemView.context, "Trebuie sa fiti autentificat pentru a putea comenta",
                    Toast.LENGTH_LONG
                ).show() else {
                    val sectiune_comentarii = Intent(itemView.context, CommentActivity::class.java)
                    sectiune_comentarii.putExtra("id_postare", adapterPosition + 1)
                    itemView.context.startActivity(sectiune_comentarii)
                }
            }
            //        if(MyPosts.active) {
//            ImageView btn=(ImageView)itemView.findViewById(R.id.menu);
//            btn.setImageResource(R.drawable.ic_baseline_delete_24);
//            itemView.findViewById(R.id.menu).setOnClickListener(view -> {
//                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                int aux=getAdapterPosition()+1;
//                mydb.delete_post(aux,MainActivity.id_user_curent);
//                notifyDataSetChanged();
//            });
//        }
            itemView.findViewById<View>(R.id.ivPost).setOnClickListener { view: View? ->
                like_counter.text = mydb.get_like_counter(adapterPosition + 1).toString()
                itemView.findViewById<View>(R.id.ivPost).isSoundEffectsEnabled = false
            }
            itemView.findViewById<View>(R.id.ivBookmark).setOnClickListener { view: View? ->
                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                if (currentFirebaseUser == null) Toast.makeText(
                    itemView.context, "Trebuie sa fiti autentificat pentru a pune un semn de carte",
                    Toast.LENGTH_SHORT
                ).show() else {
                    val aux = adapterPosition + 1
                    if (mydb.check_if_bookmark_unique(
                            aux,
                            MainActivity.Companion.id_user_curent
                        ) == 1
                    ) {
                        mydb.addBookmark(MainActivity.Companion.id_user_curent, aux)
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "Deja ati pus un semn de carte pentru aceasta postare",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            itemView.findViewById<View>(R.id.ivShare).setOnClickListener { view: View? ->
                val aux = position + 1
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                var Body = "Titlu_postare"
                var Sub = "Detalii"
                val cursor = mydb.get_title_and_content_from_post(aux)
                cursor!!.moveToNext()
                Body = cursor.getString(0)
                Sub = cursor.getString(1)
                intent.putExtra(
                    Intent.EXTRA_TEXT, """
     $Body
     $Sub
     """.trimIndent()
                )
                itemView.context.startActivity(Intent.createChooser(intent, "Trimite cu ajutorul:"))
            }
        }
    }
}