package com.dbclass.hanstagram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.databinding.ItemMessageBinding

class MessageAdapter(private val messages: MutableList<MessageEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageViewHolder).binding.textFromId.text = messages[position].fromUserID
        holder.binding.textMessageDisplay.text = messages[position].content
        holder.binding.textSentTime.text = messages[position].createdTime.toString()
    }

    inner class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)
}