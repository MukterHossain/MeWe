package com.triplesss.mewe

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.Adapter.PeopleListAdapter
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.Fragment.FragMessage
import com.triplesss.mewe.Fragment.FragPeople
import com.triplesss.mewe.Fragment.FragProfile
import com.triplesss.mewe.R.*
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind : ActivityMainBinding
    lateinit var auth: FirebaseAuth
    private val cUserModel: CurrentUserModel by viewModels()
    private var allUserList= ArrayList<UserDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

//        FireBase Declaration
        auth = Firebase.auth
        val currentUid = auth.currentUser?.uid
        val firestoRec = Firebase.firestore
        val dbRef = Firebase.database

//        Data Retrive
        firestoRec.collection("users").document(currentUid!!).get()
            .addOnSuccessListener {
                if (it != null) {
                    var user = it.toObject<UserDetails>()
                    cUserModel.setUserfmodel(user!!)
                    }
            }
//        all user data retrive and create userList array

        firestoRec.collection("users").get()
            .addOnSuccessListener {
                it.documents.forEach{
                    var user = it.toObject<UserDetails>()
                    if (currentUid != user?.uid){
                    allUserList.add(user!!)
                    }
                }
            }


//        Fragment define
        val fragMessage = FragMessage()
        val fragProfile = FragProfile()
        val fragPeople = FragPeople(allUserList)

//        ViewModel working
        cUserModel.signIn.observe(this, Observer {
            if (it.equals(false)){
                auth.signOut()
                Toast.makeText(this, "Log Out Succed", Toast.LENGTH_SHORT).show()
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