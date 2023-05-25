package com.example.proiect

class User {
    var username: String? = null
    var email: String? = null

    constructor() {}
    constructor(username: String?, email: String?) {
        this.username = username
        this.email = email
    }
}