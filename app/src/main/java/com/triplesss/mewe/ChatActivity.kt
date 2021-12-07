package com.triplesss.mewe

import android.content.ContentValues.TAG
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.Adapter.ChatListAdapter
import com.triplesss.mewe.DataModel.LastMessage
import com.triplesss.mewe.DataModel.Message
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.ActivityChatBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChatActivity : AppCompatActivity() {
    private lateinit var bind: ActivityChatBinding
    lateinit var senderObj: UserDetails
    lateinit var reciverObj: UserDetails
    private val auth = Firebase.auth
    private val cUserModel: CurrentUserModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityChatBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //firebase define
        val currentUUid = auth.currentUser?.uid
        val firestoRef = Firebase.firestore
        val docRef = Firebase.database

//        gettig User uid, name, email, Profile image etc from intent
        val reciveName = intent.getStringExtra("RecivedUserName")
        var reciverUid= intent.getStringExtra("RecivedUserUid")
        var reciverEmail= intent.getStringExtra("RecivedUserEmail")
        var reciverUri = intent.getStringExtra("ReciverUri")


//        data retrive recived User
        firestoRef.collection("users").document(reciverUid!!).get()
            .addOnSuccessListener {
                if (it != null) {
                    var user = it.toObject<UserDetails>()
                    reciverObj = user!!
                    cUserModel.setRecivedusrfmodel(user!!)
                }
            }
//        data retrive for sender obj
        firestoRef.collection("users").document(currentUUid!!).get()
            .addOnSuccessListener {
                if (it != null) {
                    var user = it.toObject<UserDetails>()
                    senderObj = user!!
                    cUserModel.setUserfmodel(user!!)
                }
            }

//        set data from intent to chat activity screen
        bind.txNameChat.text = reciveName
        bind.txEmailChat.text = reciverEmail
        Glide.with(this).load(reciverUri).placeholder(R.drawable.user).into(bind.imgProfile)

        var senderRoom : String = currentUUid.toString()+reciverUid.toString()
        var reciverRoom : String = reciverUid.toString()+currentUUid.toString()

//        back button impliment
        bind.btnBackChat.setOnClickListener {
            finish()
        }

//        all messsage retrive and show in recycle view
        var messageList = ArrayList<Message>()

        var adapter = ChatListAdapter(this,messageList,currentUUid!!)
        bind.recycleChat.setHasFixedSize(true)
        bind.recycleChat.layoutManager = LinearLayoutManager(this)
        bind.recycleChat.adapter = adapter

        docRef.reference.child("chatRoom").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    docRef.reference.child("MessageUserList").child(currentUUid).child(reciverUid).child("LastMessage").child("seen").setValue(true)
                    messageList.clear()
                    for(post in snapshot.children){
                        val message = post.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                    bind.recycleChat.scrollToPosition(messageList.size-1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        bind.txMessageBox.setOnClickListener{
            bind.recycleChat.scrollToPosition(messageList.size-1)
        }



//        message sending Process
        bind.btnSendMessage.setOnClickListener{
            var message = bind.txMessageBox.text.toString().trim()
            val randomMessKey = docRef.reference.push().key.toString()
            if(TextUtils.isEmpty(message)){
                bind.txMessageBox.setError("Not be Empty")
                return@setOnClickListener
            }


            val time = LocalDateTime.now()
            var nowtime = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            val messObject = Message(message,currentUUid.toString(),nowtime)

            var lastMessforReciver = LastMessage(message,nowtime,false)
            var lastMessforSender = LastMessage(message,nowtime,true)


            docRef.reference.child("chatRoom").child(senderRoom).child("messages").child(randomMessKey)
                .setValue(messObject).addOnSuccessListener {
                    docRef.reference.child("chatRoom").child(reciverRoom).child("messages").child(randomMessKey)
                        .setValue(messObject).addOnSuccessListener {

                            docRef.reference.child("MessageUserList").child(reciverUid!!).child(currentUUid).child("userObj").setValue(senderObj)
                            docRef.reference.child("MessageUserList").child(currentUUid).child(reciverUid!!).child("userObj").setValue(reciverObj)
                            docRef.reference.child("MessageUserList").child(currentUUid).child(reciverUid!!).child("LastMessage").setValue(lastMessforSender)
                                .addOnSuccessListener {
                                    docRef.reference.child("MessageUserList").child(reciverUid!!).child(currentUUid).child("LastMessage").setValue(lastMessforReciver)
                                }
                        }
                }
            bind.txMessageBox.setText("")

        }
    }
}