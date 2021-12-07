package com.triplesss.mewe

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.R.*
import com.triplesss.mewe.databinding.ActivityRegisterBinding
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var bind : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        if(dialog.window !=null){
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
//        Initialize firebase auth
        val auth = Firebase.auth
        val database = Firebase.firestore
//        Initialize view
        var btRegister = bind.LyRegisterBtn
        var name = bind.edtxName
        var email = bind.edtxEmail
        var mobile = bind.edtxmobile
        var dateofbirth = bind.edtxDateofbirth
        var password = bind.edtxPassword
        var signin = bind.txSignin

        var genderGroup = bind.rgGender
        var rgbMale = bind.rbgMale
        var rgbFemale = bind.rbgFemale
        var rgbOthers = bind.rbgOthers
        var gender = "Male"
        var parent = bind.lyParent

//        sign in button intent
        signin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
//        working with date
        var calendar = Calendar.getInstance()
        var mydate = calendar.get(Calendar.DAY_OF_MONTH)
        var mymonth = calendar.get(Calendar.MONTH)
        var myYear = calendar.get(Calendar.YEAR)

        dateofbirth.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val month = month + 1
                    var msg = "$dayOfMonth-$month-$year"
                    dateofbirth.setText(msg)
//                    Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_SHORT).show()
                    mydate = dayOfMonth
                    mymonth = month
                    myYear = year
                },
                myYear,
                mymonth,
                mydate
            )
            datePickerDialog.show()

        }
//working with radio button
        genderGroup.setOnCheckedChangeListener { genderGroupe, checkedId ->
            if (checkedId == rgbMale.id) {
                gender = "Male"
                return@setOnCheckedChangeListener
            } else if (checkedId == rgbFemale.id) {
                gender = "Female"
                return@setOnCheckedChangeListener
            } else if (checkedId == rgbOthers.id) {
                gender = "Others"
                return@setOnCheckedChangeListener
            }
        }

        //        email validation check
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

        //        mobile validation check
        fun isMobileValid(mobiles: String): Boolean {
            return Pattern.compile("^\\+?(88)?0?1[3456789][0-9]{8}\\b").matcher(mobiles.toString())
                .matches()
        }

        //        password valid check
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

//        Working with profile images
        bind.imgSelect.setOnClickListener {
            var intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent,100)
        }



//register button cliked
        btRegister.setOnClickListener {
            val nametext = name.text.toString()
            if (TextUtils.isEmpty(nametext)) {
                name.error = "Your Name is empty"
                return@setOnClickListener
            }
            if (nametext.length > 20) {
                name.setText("${nametext.length}")
                name.error = "Name should be Maximum 20 char"
                return@setOnClickListener
            }
            val emailtext = email.text.toString()
            if (TextUtils.isEmpty(emailtext)) {
                email.error = "Enter Email!"
                return@setOnClickListener
            }
            if (!isEmailValid(emailtext)) {
                email.error = "Enter a valid email"
                return@setOnClickListener
            }
            var mobilenum = mobile.text.toString()
            if (TextUtils.isEmpty(mobilenum)) {
                mobile.error = "Enter Mobile!"
                return@setOnClickListener
            }
            if (!isMobileValid(mobilenum)) {
                mobile.error = "Enter Valid Mobile Number"
                return@setOnClickListener
            }
            var dateOfBirth = dateofbirth.text.toString()
            if (TextUtils.isEmpty(dateOfBirth)) {
                dateofbirth.error = "Enter date of birth!"
                return@setOnClickListener
            }
            val pass = password.text.toString()
            if (TextUtils.isEmpty(pass)) {
                password.error = "Enter Password!"
                return@setOnClickListener
            }
            if (!isPasswordValid(pass)) {
                password.error = "Enter Password\n" +
                        "at least 1 digit\n" +
                        "No White spaces\n" +
                        "at least 4 to max 16 characters"
                return@setOnClickListener
            }

            dialog.show()
            auth.createUserWithEmailAndPassword(
                email.text.toString().trim(),
                password.text.toString().trim()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUserUid = auth.currentUser?.uid.toString()
                    val user = UserDetails(currentUserUid,nametext,emailtext,mobilenum,gender, dateOfBirth)

                    var dbreference : DocumentReference = database.collection("users").document(currentUserUid)
                    dbreference.set(user).addOnSuccessListener {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $currentUserUid")

                    }
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Succesfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                    var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed! Check internet Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}