package com.triplesss.mewe.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.triplesss.mewe.ChatActivity
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.R
import com.triplesss.mewe.databinding.PeoplelistRecycleBinding

class PeopleListAdapter(val context: Context, val allUserArray: ArrayList<UserDetails>) : RecyclerView.Adapter<PeopleListAdapter.myViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
        var bind = PeoplelistRecycleBinding.inflate(LayoutInflater.from(context), parent,false)
        return myViewholder(bind)
    }

    override fun onBindViewHolder(holder: myViewholder, position: Int) {

        Glide.with(context).load(allUserArray[position].profileUri).placeholder(R.drawable.user)
            .into(holder.bind.imgProfile)
        holder.bind.txNamePeople.text = allUserArray[position].name
        holder.bind.txEmailPeople.text = allUserArray[position].email

        holder.bind.icSendBtn.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("RecivedUserName",allUserArray[position].name)
            intent.putExtra("RecivedUserUid",allUserArray[position].uid)
            intent.putExtra("RecivedUserEmail",allUserArray[position].email)
            intent.putExtra("ReciverUri",allUserArray[position].profileUri)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return allUserArray.size
    }

    class myViewholder(val bind : PeoplelistRecycleBinding): RecyclerView.ViewHolder(bind.root){

    }
}