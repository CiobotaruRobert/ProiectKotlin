package com.example.proiect

import android.graphics.Bitmap

class Feed(profileIcon: Bitmap, postImage: Bitmap, title: String, message: String) {
    var profileIcon: Bitmap
    var postImage: Bitmap
    var title: String
    var message: String
    var pozitie: Int

    init {
//        pozitie = pozitie + 1
        pozitie=1
        this.profileIcon = profileIcon
        this.postImage = postImage
        this.title = title
        this.message = message
    }
}