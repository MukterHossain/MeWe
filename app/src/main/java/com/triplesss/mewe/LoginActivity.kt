package com.triplesss.mewe

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.R.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        var auth = Firebase.auth
        val currentuser = auth.currentUser

//        if (currentuser != null) {
//            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
//            finish()
//        }


//        var ParentLy = findViewById<ConstraintLayout>(id.ly_Parent_Login)
        var btLogin = findViewById<ConstraintLayout>(id.Ly_Login_btn)
        var email = findViewById<EditText>(id.edtx_Email)
        var password = findViewById<EditText>(id.edtx_Password)
        var createOne = findViewById<TextView>(id.tx_Createone_btn)

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


        btLogin.setOnClickListener {
            val mail = email.text.toString()
            if (TextUtils.isEmpty(mail)) {
                email.setError("Enter Email!")
                return@setOnClickListener
            }
            if (!isEmailValid(mail)){
                email.error = "Enter valid email"
                return@setOnClickListener
            }
            val pass = password.text.toString()
            if (TextUtils.isEmpty(pass)) {
                password.setError("Enter Password!")
                return@setOnClickListener
            }
            if (!isPasswordValid(pass)) {
                password.setError("Enter valid Pass")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        Toast.makeText(this@LoginActivity, "Login Succed",Toast.LENGTH_SHORT).show()
//                        var intent = Intent(this@LoginActivity, ProfileActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Check Internet! Login UnSucced",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        createOne.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}