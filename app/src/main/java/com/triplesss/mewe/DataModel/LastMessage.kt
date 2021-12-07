package com.triplesss.mewe.DataModel

class LastMessage {
    var seen: Boolean? = null
    var lastMessage:String?= null
    var time:String? = null

    constructor(){
    }
    constructor(lastMessage: String, time:String, seenReciver: Boolean) {
        this.seen = seenReciver
        this.lastMessage = lastMessage
        this.time = time
    }
}