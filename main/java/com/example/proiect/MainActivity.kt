package com.example.proiect

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.database.CursorWindow
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.AppBarConfiguration.Builder.build
//import androidx.navigation.ui.AppBarConfiguration.Builder.setOpenableLayout
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiect.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.lang.reflect.Field
import java.util.Locale

class MainActivity : AppCompatActivity() {
    var listView: ListView? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null
    private var mAuth: FirebaseAuth? = null
    private var recyclerView: RecyclerView? = null
    private var arrayList: ArrayList<Feed>? = null
    private var searchView: SearchView? = null
    private var recyclerAdapter: RecyclerAdapter? = null
    var mydb: DatabaseHelper? = null
    var id_postare: ArrayList<String>? = null
    var titlu_postare: ArrayList<String>? = null
    var mesaj_postare: ArrayList<String>? = null
    var poza_postare: ArrayList<Bitmap>? = null
    var chei_useri: ArrayList<String?>? = null
    var textview_nav_header_title: TextView? = null
    var imageView_nav_header: ImageView? = null
    var barray_aux: ArrayList<Bitmap>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val inflatedView = layoutInflater.inflate(R.layout.nav_header_main, null)
        textview_nav_header_title =
            inflatedView.findViewById<View>(R.id.nav_header_title) as TextView
        textview_nav_header_title!!.text = "AAAAAAAA"

        fun animateViewScale(view: View, scale: Float, duration: Int) {
            val animator = ObjectAnimator.ofFloat(view, "scaleX", scale)
            animator.duration = duration.toLong()
            animator.start()
        }
        setSupportActionBar(binding!!.appBarMain.toolbar)
        binding!!.appBarMain.logoutbutton.setOnClickListener { view ->
            if (mAuth!!.currentUser == null) {
                val intent = Intent(view.context, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                logoutUser()
            }
        }
        val drawer = binding!!.drawerLayout
        val navigationView = binding!!.navView
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_contact, R.id.nav_add_post, R.id.nav_bookmarks
        )
            .setOpenableLayout(drawer)
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.nav_home) {
                val newIntent = Intent(baseContext, MainActivity::class.java)
                startActivity(newIntent)
            }
            if (id == R.id.nav_contact) {
                val newIntent = Intent(baseContext, ContactActivity::class.java)
                startActivity(newIntent)
            }
            if (mAuth!!.currentUser == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Trebuie sa fiti autentificat pentru a accesa aceasta pagina",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (id == R.id.nav_my_posts) {
                    val newIntent = Intent(baseContext, MyPosts::class.java)
                    startActivity(newIntent)
                }
                if (id == R.id.nav_add_post) {
                    val newIntent = Intent(baseContext, AddPost::class.java)
                    startActivity(newIntent)
                }
                if (id == R.id.nav_bookmarks) {
                    val newIntent = Intent(baseContext, BookmarkedPosts::class.java)
                    startActivity(newIntent)
                }
            }
            true
        }

        mydb = DatabaseHelper(this@MainActivity)
        id_postare = ArrayList()
        titlu_postare = ArrayList()
        mesaj_postare = ArrayList()
        poza_postare = ArrayList()
        var field: Field? = null
        try {
            field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        field!!.isAccessible = true
        try {
            field[null] = 100 * 1024 * 1024
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        storeDataInArrays()
        arrayList = ArrayList()
        recyclerView = findViewById(R.id.recyclerView)
        for (i in titlu_postare!!.indices) {
            val cursor = mydb!!.get_owner_user_key(i + 1)
            cursor!!.moveToNext()
            val aux_cheie = cursor.getString(0)
            val cursor2 = mydb!!.get_profile_image_post(aux_cheie)
            cursor2!!.moveToNext()
            val bmp = BitmapFactory.decodeByteArray(cursor2.getBlob(0), 0, cursor2.getBlob(0).size)
            arrayList!!.add(Feed(bmp, poza_postare!![i], titlu_postare!![i], mesaj_postare!![i]))
        }
        recyclerAdapter = RecyclerAdapter(arrayList!!)
        recyclerView?.setAdapter(recyclerAdapter)
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
        var currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            id_user_curent = currentFirebaseUser.uid
            val mydb = DatabaseHelper(this@MainActivity)
            val cursor = mydb.get_user_keys()
            chei_useri = ArrayList()
            while (cursor!!.moveToNext()) chei_useri!!.add(cursor!!.getString(0))

            if (!chei_useri!!.contains(id_user_curent)) {
                mydb.insert_user_key(
                    id_user_curent,
                    RegisterActivity.usern,
                    RegisterActivity.barray_aux
                )
            }
            cursor!!.close()
        }
        searchView = findViewById(R.id.searchview)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                val myView = findViewById<View>(R.id.searchview)
                animateViewScale(myView, 1.2f, 1000)
                return true
            }
        })
    }

    private fun filterList(text: String) {
        val filteredList = ArrayList<Feed>()
        for (item in arrayList!!) {
            if (item.message.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault())) || item.title.lowercase(
                    Locale.getDefault()
                ).contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Nu am gasit nimic", Toast.LENGTH_SHORT).show()
        } else {
            recyclerAdapter!!.setFilteredList(filteredList)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
        //finish();
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    fun storeDataInArrays() {
        val cursor = mydb!!.readAllData()
        if (cursor!!.count == 0) {
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
        var id_user_curent: String? = null
    }
}