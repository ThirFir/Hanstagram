package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.repository.MessageRepository
import com.dbclass.hanstagram.databinding.ItemMessageBinding
import com.dbclass.hanstagram.databinding.ItemMessageExpandableBinding
import com.skydoves.expandablelayout.ExpandableLayout
import com.skydoves.expandablelayout.expandableLayout

class MessageAdapter(private var messages: MutableList<MessageEntity>, private val myID: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        (holder as MessageViewHolder).binding.run {
            textSentTime.text = message.createdTime.toString()
            textMessage.text = message.content.toString()
            if(myID == message.fromUserID)
                textId.text = "To : " + message.toUserID
            else
                textId.text = "From : " + message.fromUserID
        }
    }

    fun updateMessages(messages: List<MessageEntity>) {
        this.messages = mutableListOf()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }
    inner class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)
}