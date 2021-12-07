package com.triplesss.mewe

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.R.*
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private var auth = Firebase.auth
    lateinit var bind : ActivityLoginBinding
    private val cUserModel : CurrentUserModel by viewModels()
    var currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val dialog = Dialog(this)
        dialog.setContentView(layout.dialog_loading)
        dialog.setCancelable(false)
        if(dialog.window !=null){
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        if (currentUser != null) {
            cUserModel.setSignin(true)

            }

        cUserModel.signIn.observe(this, Observer {
            if (it.equals(true)){
                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        //password validation check
        fun isPasswordValid(pass: String): Boolean {
            return Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{6,16}" +               //at least 4 to max 16 characters
                        "$"
            ).matcher(pass).matches()
        }

        //        email validation
        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }


        bind.LyLoginBtn.setOnClickListener {
            val mail = bind.edtxEmail.text.toString()
            if (TextUtils.isEmpty(mail)) {
                bind.edtxEmail.setError("Enter Email!")
                return@setOnClickListener
            }
            if (!isEmailValid(mail)){
                bind.edtxEmail.error = "Enter valid email"
                return@setOnClickListener
            }
            val pass = bind.edtxPassword.text.toString()
            if (TextUtils.isEmpty(pass)) {
                bind.edtxPassword.setError("Enter Password!")
                return@setOnClickListener
            }
            if (!isPasswordValid(pass)) {
                bind.edtxPassword.setError("Enter valid Pass")
                return@setOnClickListener
            }
            dialog.show()
            auth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        cUserModel.setSignin(true)
                        dialog.dismiss()
//                        Toast.makeText(this@LoginActivity, "Logged",Toast.LENGTH_SHORT).show()
                    } else {
                        cUserModel.setSignin(false)
                        dialog.dismiss()
                        Toast.makeText(this@LoginActivity, "Check Internet! Login UnSucced",Toast.LENGTH_SHORT).show()
                    }
                }
        }
        bind.txCreateoneBtn.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}