package com.triplesss.mewe.DataModel

import android.net.Uri

class UserDetails {
    lateinit var uid :String
    lateinit var profileUri :String
    lateinit var name:String
    lateinit var email:String
    lateinit var mobile:String
    lateinit var gender:String
    lateinit var dateOfBirth:String

    constructor(){}

    constructor(
        uid: String,
        profileUri: String,
        name: String,
        email: String,
        mobile: String,
        gender: String,
        dateOfBirth: String
    ) {
        this.uid = uid
        this.profileUri = profileUri
        this.name = name
        this.email = email
        this.mobile = mobile
        this.gender = gender
        this.dateOfBirth = dateOfBirth
    }
}