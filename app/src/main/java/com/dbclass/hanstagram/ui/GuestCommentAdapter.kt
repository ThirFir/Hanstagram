package com.dbclass.hanstagram.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.repository.GuestCommentRepository
import com.dbclass.hanstagram.databinding.ItemGuestBinding
import java.text.SimpleDateFormat
import java.util.*

class GuestAdapter(private val guestComments: MutableList<GuestCommentEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GuestViewHolder(ItemGuestBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = guestComments.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as GuestViewHolder
        holder.binding.textGuestId.text = guestComments[position].guestUserID
        holder.binding.textGuestComment.text = guestComments[position].comment
        holder.binding.textEditDate.text = SimpleDateFormat("yyyy-MMM-dd HH:mm").format(Date(guestComments[position].createdTime))
    }

    fun addComment(guestComment: GuestCommentEntity) {
        GuestCommentRepository.addComment(guestComment)
        guestComments.add(0, guestComment)
        notifyItemInserted(0)
    }
    inner class GuestViewHolder(val binding: ItemGuestBinding): RecyclerView.ViewHolder(binding.root)
}