package com.triplesss.mewe.Fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.LoginActivity
import com.triplesss.mewe.R
import com.triplesss.mewe.R.*
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.FragmentProfileBinding
import android.content.Intent as Intent

class FragProfile() : Fragment() {
    private lateinit var bind : FragmentProfileBinding
    private val cUserModel: CurrentUserModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentProfileBinding.inflate(layoutInflater,container,false)
        var view = bind.root


//        oberve current user model data
        cUserModel.currentUserObj.observe(viewLifecycleOwner,{
                bind.txNameBox.text = it?.name
                bind.txEmailBox.text = it?.email
                bind.txMobileBox.text = it?.mobile
                bind.txGenderBox.text = it?.gender
                bind.txDobBox.text = it?.dateOfBirth
            Glide.with(context!!).load(it?.profileUri)
                .placeholder(R.drawable.user)
                .into(bind.imageProfile)

        })


//        logout button clicked control
        bind.LyLogoutBtn.setOnClickListener {
            cUserModel.setSignin(false)
        }

            return view
    }

}

