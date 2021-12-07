package com.triplesss.mewe

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.Adapter.PeopleListAdapter
import com.triplesss.mewe.DataModel.LastMessage
import com.triplesss.mewe.DataModel.Message
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.Fragment.FragMessage
import com.triplesss.mewe.Fragment.FragPeople
import com.triplesss.mewe.Fragment.FragProfile
import com.triplesss.mewe.R.*
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var bind : ActivityMainBinding
    lateinit var auth: FirebaseAuth
    private val cUserModel: CurrentUserModel by viewModels()
    private var allUserList= ArrayList<UserDetails>()
    private var lasMessList= ArrayList<LastMessage>()
    private var messUserList= ArrayList<UserDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        if(dialog.window !=null){
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
//        FireBase Declaration
        auth = Firebase.auth
        val currentUid = auth.currentUser?.uid
        val firestoRef = Firebase.firestore
        val docRef = Firebase.database



//        Data Retrive current usr
        firestoRef.collection("users").document(currentUid!!).get()
            .addOnSuccessListener {
                if (it != null) {
                    var user = it.toObject<UserDetails>()
                    if (user?.email != null) {
                        cUserModel.setUserfmodel(user!!)

                    }
                }
            }
//        all user data retrive and create userList array

        firestoRef.collection("users").get()
            .addOnSuccessListener {
                it.documents.forEach{
                    var user = it.toObject<UserDetails>()
                    if (currentUid != user?.uid){
                        allUserList.add(user!!)
                    }
                }
            }



        //better way to retrive userkey list
        docRef.reference.child("MessageUserList").child(currentUid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messUserList.clear()
                lasMessList.clear()
                for(user in snapshot.children){
                    var userObj = user.child("userObj").getValue(UserDetails::class.java)
                    var messObj = user.child("LastMessage").getValue(LastMessage::class.java)
                    messUserList.add(userObj!!)
                    lasMessList.add(messObj!!)
                    dialog.dismiss()
                }
//                Log.d(TAG,"user ${messUserList}")
//                Log.d(TAG,"Last mess ${lasMessList}")
                cUserModel.setmessUserList(messUserList)
                cUserModel.setlastMessList(lasMessList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

//        Log.d(TAG,"user outside ${messUserList}")
//        Log.d(TAG,"Last mess outside ${lasMessList}")


//        Fragment define
        val fragMessage = FragMessage()
        val fragProfile = FragProfile()
        val fragPeople = FragPeople(allUserList)

//        ViewModel working
        cUserModel.signIn.observe(this, Observer {
            if (it.equals(false)){
                auth.signOut()
//                Toast.makeText(this, "Log Out Succed", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

//        Home Screen Setup
        val fragManager = supportFragmentManager
        fragManager.beginTransaction().replace(id.frag_Main,fragProfile).commit()

//        navigation button
        val scrMessage = bind.btnMessageTx
        val scrProfile = bind.btnProfileTx
        val scrPeople = bind.btnPeopleTx

//        variable define
        val colorPrimary = Color.parseColor("#FFA0B4")
        val primaryBackRound = drawable.bg_primary_nostok_100
        val formBackRound10 = drawable.profile_form_backgroud

//        message Screen setup
        scrMessage.setOnClickListener{
            fragManager.beginTransaction().replace(id.frag_Main,fragMessage).commit()
//            Message button
            scrMessage.setTextColor(Color.WHITE)
            scrMessage.setBackgroundResource(primaryBackRound)
//            Profile button
            scrProfile.setTextColor(colorPrimary)
            scrProfile.setBackgroundResource(formBackRound10)
//            people button
            scrPeople.setTextColor(colorPrimary)
            scrPeople.setBackgroundResource(formBackRound10)

            scrMessage.setTypeface(Typeface.DEFAULT_BOLD);

        }
//        Profile Screen setup
        scrProfile.setOnClickListener{
            fragManager.beginTransaction().replace(id.frag_Main,fragProfile).commit()
//            Profile button
            scrProfile.setTextColor(Color.WHITE)
            scrProfile.setBackgroundResource(primaryBackRound)
            //            Message button
            scrMessage.setTextColor(colorPrimary)
            scrMessage.setBackgroundResource(formBackRound10)
//            people button
            scrPeople.setTextColor(colorPrimary)
            scrPeople.setBackgroundResource(formBackRound10)

            scrPeople.setTypeface(Typeface.DEFAULT_BOLD);

        }
        //        People Screen setup
        scrPeople.setOnClickListener{
            fragManager.beginTransaction().replace(id.frag_Main,fragPeople).commit()
//            people button
            scrPeople.setTextColor(Color.WHITE)
            scrPeople.setBackgroundResource(primaryBackRound)
            //            Message button
            scrMessage.setTextColor(colorPrimary)
            scrMessage.setBackgroundResource(formBackRound10)
//            Profile button
            scrProfile.setTextColor(colorPrimary)
            scrProfile.setBackgroundResource(formBackRound10)

            scrPeople.setTypeface(Typeface.DEFAULT_BOLD);
        }



    }
}