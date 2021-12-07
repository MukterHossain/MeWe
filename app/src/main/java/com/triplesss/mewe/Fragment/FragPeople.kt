package com.triplesss.mewe.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.triplesss.mewe.Adapter.PeopleListAdapter
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.ViewModel.CurrentUserModel
import com.triplesss.mewe.databinding.FragmentPeopleBinding

class FragPeople(): Fragment() {
    private val cUserModel : CurrentUserModel by activityViewModels()
    private lateinit var bind : FragmentPeopleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentPeopleBinding.inflate(layoutInflater,container,false)
        bind.lyRecyclePeople.layoutManager = LinearLayoutManager(context)
        cUserModel.allUserList.observe(viewLifecycleOwner, Observer {
            var allUserList = it
            var adapter =PeopleListAdapter(context!!,allUserList)
            bind.lyRecyclePeople.adapter =adapter
        })

        return bind.root
    }
}