package com.triplesss.mewe.Fragment

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.triplesss.mewe.Adapter.MessageListAdapter
import com.triplesss.mewe.DataModel.LastMessage
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.FragmentMessageBinding

class FragMessage(): Fragment() {

    private val cUserModel: CurrentUserModel by activityViewModels()
    private lateinit var bind : FragmentMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        bind = FragmentMessageBinding.inflate(LayoutInflater.from(context),container,false)
        bind.lyRecycleMessage.layoutManager = LinearLayoutManager(context)

        cUserModel.messUserList.observe(viewLifecycleOwner, Observer {
            var messUserList = it
            var lastMessList = cUserModel.lastMessList.value
            if(lastMessList !== null && messUserList !=null){
                var adapter = MessageListAdapter(context!!,messUserList,lastMessList)
                bind.lyRecycleMessage.adapter = adapter
            }
        })


        return bind.root
    }
}