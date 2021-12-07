package com.triplesss.mewe.DataModel

class Message {
    lateinit var message: String
    lateinit var senderId: String
    lateinit var timeStamp: String

    constructor(){
    }

    constructor(message: String, senderId: String, timeStamp: String) {
        this.message = message
        this.senderId = senderId
        this.timeStamp = timeStamp
    }
}