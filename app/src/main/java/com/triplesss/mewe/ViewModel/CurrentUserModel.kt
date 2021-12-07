package com.triplesss.mewe.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.triplesss.mewe.DataModel.LastMessage
import com.triplesss.mewe.DataModel.UserDetails

class CurrentUserModel :  ViewModel(){
    val currentUserObj = MutableLiveData<UserDetails>()
    val recivedUserObj = MutableLiveData<UserDetails>()
    val signIn = MutableLiveData<Boolean>()
    val lastMessList = MutableLiveData<ArrayList<LastMessage>>()
    val messUserList = MutableLiveData<ArrayList<UserDetails>>()

    fun setUserfmodel(newData : UserDetails){
        currentUserObj.value = newData
    }
    fun setRecivedusrfmodel(newData : UserDetails){
        recivedUserObj.value = newData
    }
    fun setSignin(signData : Boolean){
        signIn.value = signData
    }
    fun setlastMessList(data : ArrayList<LastMessage>){
        lastMessList.value = data
    }
    fun setmessUserList(data : ArrayList<UserDetails>){
        messUserList.value = data
    }
}