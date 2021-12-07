package com.triplesss.mewe.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.triplesss.mewe.DataModel.Message
import com.triplesss.mewe.databinding.MessageListBinding

class ChatListAdapter(context: Context, messageList: ArrayList<Message>,currentUUid:String): RecyclerView.Adapter<ChatListAdapter.myViewHolder>(){
    val context = context
    val messageList = messageList
    val cUserId = currentUUid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val layout = MessageListBinding.inflate(LayoutInflater.from(context),parent,false)

        return myViewHolder(layout)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        var SenderId = messageList[position].senderId
        if (SenderId.equals(cUserId)){
            holder.bind.txSendMes.setText(messageList[position].message)
            holder.bind.txTimeSend.setText(messageList[position].timeStamp)
            holder.bind.txReciveMes.visibility = View.GONE
            holder.bind.txTimeRecived.visibility = View.GONE
            holder.bind.txSendMes.visibility = View.VISIBLE
            holder.bind.txTimeSend.visibility = View.VISIBLE
        }else {
            holder.bind.txReciveMes.setText(messageList[position].message)
            holder.bind.txTimeRecived.setText(messageList[position].timeStamp)
            holder.bind.txReciveMes.visibility = View.VISIBLE
            holder.bind.txTimeRecived.visibility = View.VISIBLE
            holder.bind.txSendMes.visibility = View.GONE
            holder.bind.txTimeSend.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class myViewHolder(val bind : MessageListBinding):RecyclerView.ViewHolder(bind.root){

    }
}