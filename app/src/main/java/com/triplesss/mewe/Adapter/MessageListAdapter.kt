package com.triplesss.mewe.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        holder.bind.imgProfile.setImageResource(R.drawable.user)
        holder.bind.txNameMessage.text = userList[position].name
        holder.bind.txLastMessage.text = lastMessage[position].lastMessage
        holder.bind.txTimeShow.text = lastMessage[position].time
        holder.bind.txTimeShow.visibility = View.VISIBLE
        if(lastMessage[position].seen!!.equals(false)){
            holder.bind.txTimeShow.setTextColor(R.color.color_main)
            holder.bind.txLastMessage.setTextSize(14F)
        }

        holder.bind.lyParentMessageListup.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("RecivedUserName",userList[position].name)
            intent.putExtra("RecivedUserUid",userList[position].uid)
            intent.putExtra("RecivedUserEmail",userList[position].email)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class myViewHolder(val bind : MessageScrListBinding):RecyclerView.ViewHolder(bind.root) {

    }
}