package com.triplesss.mewe

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.Fragment.FragMessage
import com.triplesss.mewe.Fragment.FragPeople
import com.triplesss.mewe.Fragment.FragProfile
import com.triplesss.mewe.R.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

//        FireBase Declaration
        auth = Firebase.auth
        val currentUid = auth.currentUser?.uid
        val firestoRec = Firebase.firestore
        val dbRef = Firebase.database

//        view define

//        Fragment define
        val fragMessage = FragMessage()
        val fragProfile = FragProfile()
        val fragPeople = FragPeople()
//        Home Screen Setup
        val fragManager = supportFragmentManager
        fragManager.beginTransaction().replace(id.frag_Main,fragProfile).commit()

//        navigation button
        val scrMessage = findViewById<TextView>(id.btn_Message_tx)
        val scrProfile = findViewById<TextView>(id.btn_Profile_tx)
        val scrPeople = findViewById<TextView>(id.btn_People_tx)

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