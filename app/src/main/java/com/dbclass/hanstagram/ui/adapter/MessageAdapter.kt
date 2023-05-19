package com.dbclass.hanstagram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.utils.getFormattedDate
import com.dbclass.hanstagram.databinding.ItemMessageBinding

class MessageAdapter(private var messages: MutableList<MessageEntity>, private val myID: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        (holder as MessageViewHolder).binding.run {
            textSentTime.text = getFormattedDate(message.createdTime)
            textMessage.text = message.content.toString()
            if(myID == message.fromUserID)
                textId.text = "To - " + message.toUserID
            else
                textId.text = "From - " + message.fromUserID
        }
    }

    fun updateMessages(messages: List<MessageEntity>) {
        this.messages = mutableListOf()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }
    inner class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)
}