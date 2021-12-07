package com.triplesss.mewe.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.triplesss.mewe.ChatActivity
import com.triplesss.mewe.DataModel.LastMessage
import com.triplesss.mewe.DataModel.UserDetails
import com.triplesss.mewe.Fragment.FragMessage
import com.triplesss.mewe.R
import com.triplesss.mewe.databinding.MessageScrListBinding
import org.w3c.dom.Text

class MessageListAdapter(context: Context, userList: ArrayList<UserDetails>, lastMessage: ArrayList<LastMessage>):
    RecyclerView.Adapter<MessageListAdapter.myViewHolder>() {

    var context = context
    var userList = userList
    var lastMessage = lastMessage


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        var bind = MessageScrListBinding.inflate(LayoutInflater.from(context),parent,false)
        return myViewHolder(bind)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        Glide.with(context).load(userList[position].profileUri).placeholder(R.drawable.user)
            .into(holder.bind.imgProfile)
        holder.bind.txNameMessage.text = userList[position].name
        holder.bind.txLastMessage.text = lastMessage[position].lastMessage
        holder.bind.txTimeShow.text = lastMessage[position].time
        holder.bind.txTimeShow.visibility = View.VISIBLE
        if(lastMessage[position].seen!! == false){
            holder.bind.txTimeShow.setTextColor(Color.parseColor("#FF0000"))
//            holder.bind.txLastMessage.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        }

        holder.bind.lyParentMessageListup.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("RecivedUserName",userList[position].name)
            intent.putExtra("RecivedUserUid",userList[position].uid)
            intent.putExtra("RecivedUserEmail",userList[position].email)
            intent.putExtra("ReciverUri",userList[position].profileUri)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class myViewHolder(val bind : MessageScrListBinding):RecyclerView.ViewHolder(bind.root) {

    }
}