package com.triplesss.mewe

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.databinding.ActivityChatBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChatActivity : AppCompatActivity() {
    private lateinit var bind: ActivityChatBinding
    private val auth = Firebase.auth
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityChatBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //firebase define
        val currentUUid = auth.currentUser?.uid
        val firestoRef = Firebase.firestore
        val docRef = Firebase.database

//        back button impliment
        bind.btnBackChat.setOnClickListener {
            finish()
        }

//        message sending Process
        bind.btnSendMessage.setOnClickListener{
            var message = bind.txMessageBox.text.toString().trim()
            if(TextUtils.isEmpty(message)){
                bind.txMessageBox.setError("Not be Empty")
                return@setOnClickListener
            }

            val time = LocalDateTime.now()
            var nowtime = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            val messObject = Messages(currentUserUid.toString(),message,nowtime)

            dbReference.child("chatRoom").child(senderRoom).child("messages").push()
                .setValue(messObject).addOnSuccessListener {
                    dbReference.child("chatRoom").child(reciverRoom).child("messages").push()
                        .setValue(messObject).addOnSuccessListener {
                            var lastMessage = LastMessage(false,message,nowtime)
                            dbReference.child("MessageUserList").child(currentUserUid).child(reciverUid!!).setValue(lastMessage)
                                .addOnSuccessListener {
                                    dbReference.child("MessageUserList").child(reciverUid!!).child(currentUserUid).setValue(lastMessage)
                                }
                            Toast.makeText(this,"ReciverRoom Updated", Toast.LENGTH_SHORT).show()
                        }
//                Toast.makeText(this,"senderRoom Updated",Toast.LENGTH_SHORT).show()
                }
            bind.txMessageBox.setText("")



        }
    }
}