package com.example.proiect

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(private val context: Context?) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, "
                + COLUMN_MESSAGE + " TEXT, "
                + COLUMN_POST_OWNER_KEY + " TEXT, " +
                COLUMN_LIKE_COUNTER + " INTEGER DEFAULT 0, " +
                COLUMN_IMAGINE_BLOB + " BLOB " + ");")
        db.execSQL(query)
        val query2 = "CREATE TABLE " + TABLE_NAME_2 +
                " (" + COLUMN_ID_2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_KEY + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_IMAGINE_PROFIL_BLOB + " BLOB " + ");"
        db.execSQL(query2)
        val query3 = "CREATE TABLE " + TABLE_NAME_BOOKMARKS +
                " (" + COLUMN_ID_TABEL_BOOKMARKS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_POSTARE_BOOKMARKS_TABLE + " TEXT, " +
                COLUMN_USER_KEY_BOOKMARKS_TABLE + " TEXT);"
        db.execSQL(query3)
        val query4 = "CREATE TABLE " + TABLE_NAME_UNIQUE_LIKES +
                " (" + COLUMN_ID_TABEL_UNIQUE_LIKES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_POSTARE_UNIQUE_LIKES + " INTEGER, " +
                COLUMN_CHEIE_USER_UNIQUE_LIKES + " TEXT);"
        db.execSQL(query4)
        val query5 = "CREATE TABLE " + TABLE_NAME_COMMENTS +
                " (" + COLUMN_ID_TABEL_COMMENTS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_POSTARE_COMMENTS_TABLE + " INTEGER, " +
                COLUMN_TABEL_COMMENTS_MESSAGE + " TEXT, " +
                COLUMN_TABEL_COMMENTS_DATA_POSTARE + " TEXT, " +
                COLUMN_TABEL_COMMENTS_USER + " TEXT, " +
                COLUMN_TABEL_COMMENTS_POSTER_KEY + " TEXT);"
        db.execSQL(query5)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BOOKMARKS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_UNIQUE_LIKES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COMMENTS)
        onCreate(db)
    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM " + TABLE_NAME
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_user_keys(): Cursor? {
        val query = "SELECT " + COLUMN_USER_KEY + " FROM " + TABLE_NAME_2
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_profile_image_post(id_aux: String?): Cursor? {
        val query =
            "SELECT " + COLUMN_IMAGINE_PROFIL_BLOB + " FROM " + TABLE_NAME_2 + " WHERE " + COLUMN_USER_KEY + " = " + "'" + id_aux + "'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_owner_user_key(id_aux: Int): Cursor? {
        val query =
            "SELECT " + COLUMN_POST_OWNER_KEY + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id_aux
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_my_posts(aux: String?): Cursor? {
        val query =
            "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_POST_OWNER_KEY + " = " + "'" + aux + "'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun insert_user_key(cheie: String?, nume_utilizator: String?, barray: ByteArray?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_USER_KEY, cheie)
        cv.put(COLUMN_USERNAME, nume_utilizator)
        cv.put(COLUMN_IMAGINE_PROFIL_BLOB, barray)
        val result = db.insert(TABLE_NAME_2, null, cv)
    }

    fun addPost(titlu: String?, mesaj: String?, cheie_user_detinator: String?, barray: ByteArray?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_TITLE, titlu)
        cv.put(COLUMN_MESSAGE, mesaj)
        cv.put(COLUMN_POST_OWNER_KEY, cheie_user_detinator)
        cv.put(COLUMN_IMAGINE_BLOB, barray)
        val result = db.insert(TABLE_NAME, null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Nu a reusit", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Postare adaugata cu succes", Toast.LENGTH_SHORT).show()
        }
    }

    fun get_post_index(titlu: String, mesaj: String, cheie_user_detinator: String): Cursor? {
        val query =
            ("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_POST_OWNER_KEY + " = " + "'" + cheie_user_detinator + "'"
                    + " AND " + COLUMN_TITLE + " = " + "'" + titlu + "'"
                    + " AND " + COLUMN_MESSAGE + " = " + "'" + mesaj + "'")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun addBookmark(cheie_user: String?, id_postare: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_USER_KEY_BOOKMARKS_TABLE, cheie_user)
        cv.put(COLUMN_ID_POSTARE_BOOKMARKS_TABLE, id_postare)
        val result = db.insert(TABLE_NAME_BOOKMARKS, null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Nu a reusit", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Semn de carte adaugat cu succes", Toast.LENGTH_SHORT).show()
        }
    }

    fun get_id_bookmarked_by_current_user(cheie_user: String?): Cursor? {
        val query =
            ("SELECT " + COLUMN_ID_POSTARE_BOOKMARKS_TABLE + " FROM " + TABLE_NAME_BOOKMARKS + " WHERE "
                    + COLUMN_USER_KEY_BOOKMARKS_TABLE + " = " + "'" + cheie_user + "'")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_post_bookmarked_by_current_user(id: String): Cursor? {
        val aux = id.toInt()
        val query = ("SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + aux)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun add_like_to_post(id_input: Int, cheie_user: String?) {
        val db = this.writableDatabase
        db.execSQL("UPDATE postare SET like_counter=like_counter+1 WHERE id = $id_input")
        db.execSQL("INSERT INTO unique_likes(id_postare,cheie_user) VALUES ($id_input,'$cheie_user');")
    }

    fun undo_like_to_post(id_input: Int, cheie_user: String?) {
        val db = this.writableDatabase
        db.execSQL("UPDATE postare SET like_counter=like_counter-1 WHERE id = $id_input")
        db.execSQL(
            "DELETE FROM " + TABLE_NAME_UNIQUE_LIKES + " WHERE " + COLUMN_ID_POSTARE_UNIQUE_LIKES + " = " + id_input +
                    " AND " + COLUMN_CHEIE_USER_UNIQUE_LIKES + " = " + "'" + cheie_user + "'"
        )
    }

    fun check_if_like_unique(id_input: Int, cheie_user: String?): Int {
        val query = ("SELECT count(*) FROM " + TABLE_NAME_UNIQUE_LIKES + " WHERE "
                + COLUMN_ID_POSTARE_UNIQUE_LIKES + " = " + id_input + " AND " +
                COLUMN_CHEIE_USER_UNIQUE_LIKES + " = " + "'" + cheie_user + "'")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val aux = cursor.getInt(0)
        return if (aux == 0) 1 else 0
    }

    fun check_if_bookmark_unique(id_input: Int, cheie_user: String?): Int {
        val query = ("SELECT count(*) FROM " + TABLE_NAME_BOOKMARKS + " WHERE "
                + COLUMN_ID_POSTARE_BOOKMARKS_TABLE + " = " + id_input + " AND " +
                COLUMN_USER_KEY_BOOKMARKS_TABLE + " = " + "'" + cheie_user + "'")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val aux = cursor.getInt(0)
        return if (aux == 0) 1 else 0
    }

    fun get_title_and_content_from_post(id: Int): Cursor? {
        val query = ("SELECT titlu,mesaj FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + id)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun delete_post(id_input: Int, cheie_user: String) {
        val db = this.writableDatabase
        db.execSQL(
            "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id_input +
                    " AND " + COLUMN_POST_OWNER_KEY + " = " + "'" + cheie_user + "'"
        )
    }

    fun get_like_counter(id_input: Int): Int {
        val query = ("SELECT like_counter FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + id_input)
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        return if (result.moveToFirst()) result.getInt(0) else 0
    }

    fun readMessagesFromCommentsTable(id_postare: Int): Cursor? {
        val query =
            "SELECT mesaj,data,username,cheie_postator FROM " + TABLE_NAME_COMMENTS + " WHERE " + COLUMN_ID_POSTARE_COMMENTS_TABLE + " = " + id_postare
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun PostComment(
        id_postare: Int,
        mesaj: String,
        data_comment: String,
        username: String,
        cheie_aux: String?
    ) {
        val db = this.writableDatabase
        db.execSQL("INSERT INTO comments(id_postare,mesaj,data,username,cheie_postator) VALUES ($id_postare,'$mesaj','$data_comment','$username','$cheie_aux');")
    }

    fun get_current_username(aux: String?): Cursor? {
        val query =
            "SELECT username FROM " + TABLE_NAME_2 + " WHERE " + COLUMN_USER_KEY + " = " + "'" + aux + "'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_current_user_id(aux: String): Cursor? {
        val query =
            "SELECT id FROM " + TABLE_NAME_2 + " WHERE " + COLUMN_USER_KEY + " = " + "'" + aux + "'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun get_imagine_profil(cheie_user: String?): Cursor? {
        val query =
            "SELECT " + COLUMN_IMAGINE_PROFIL_BLOB + " FROM " + TABLE_NAME_2 + " WHERE " + COLUMN_USER_KEY + " = " + "'" + cheie_user + "'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    companion object {
        private const val DATABASE_NAME = "proiect.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "postare"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "titlu"
        private const val COLUMN_MESSAGE = "mesaj"
        private const val COLUMN_LIKE_COUNTER = "like_counter"
        private const val COLUMN_POST_OWNER_KEY = "cheie_user_detinator"
        private const val COLUMN_IMAGINE_BLOB = "imagine_blob"
        private const val TABLE_NAME_2 = "user"
        private const val COLUMN_ID_2 = "id"
        private const val COLUMN_USER_KEY = "cheie"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_IMAGINE_PROFIL_BLOB = "imagine_profil"
        private const val TABLE_NAME_BOOKMARKS = "bookmarks"
        private const val COLUMN_ID_TABEL_BOOKMARKS = "id_bookmark"
        private const val COLUMN_ID_POSTARE_BOOKMARKS_TABLE = "id_postare"
        private const val COLUMN_USER_KEY_BOOKMARKS_TABLE = "cheie_user"
        private const val TABLE_NAME_UNIQUE_LIKES = "unique_likes"
        private const val COLUMN_ID_TABEL_UNIQUE_LIKES = "id_like"
        private const val COLUMN_ID_POSTARE_UNIQUE_LIKES = "id_postare"
        private const val COLUMN_CHEIE_USER_UNIQUE_LIKES = "cheie_user"
        private const val TABLE_NAME_COMMENTS = "comments"
        private const val COLUMN_ID_TABEL_COMMENTS = "id"
        private const val COLUMN_ID_POSTARE_COMMENTS_TABLE = "id_postare"
        private const val COLUMN_TABEL_COMMENTS_MESSAGE = "mesaj"
        private const val COLUMN_TABEL_COMMENTS_DATA_POSTARE = "data"
        private const val COLUMN_TABEL_COMMENTS_USER = "username"
        private const val COLUMN_TABEL_COMMENTS_POSTER_KEY = "cheie_postator"
    }
}