package com.example.proiect

class ModelComment {

    var comment: String? = null
    var ptime: String? = null
    var uname: String? = null
    var uid: String? = null

    constructor() {}
    constructor(comment: String?, ptime: String?, uid: String?, uname: String?) {
        this.comment = comment
        this.ptime = ptime
        this.uid = uid
        this.uname = uname
    }
}