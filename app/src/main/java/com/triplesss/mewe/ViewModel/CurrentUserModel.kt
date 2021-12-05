package com.triplesss.mewe.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.triplesss.mewe.DataModel.UserDetails

class CurrentUserModel :  ViewModel(){
    val currentUserObj = MutableLiveData<UserDetails>()
    val signIn = MutableLiveData<Boolean>()

    fun setUserfmodel(newData : UserDetails){
        currentUserObj.value = newData
    }
    fun setSignin(signData : Boolean){
        signIn.value = signData
    }
}