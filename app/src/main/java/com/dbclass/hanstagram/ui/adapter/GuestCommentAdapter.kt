package com.dbclass.hanstagram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.repository.GuestCommentRepository
import com.dbclass.hanstagram.databinding.ItemGuestCommentBinding
import java.text.SimpleDateFormat
import java.util.*

class GuestAdapter(private val guestComments: MutableList<GuestCommentEntity>, private val watchingUserID: String?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GuestViewHolder(ItemGuestCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = guestComments.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as GuestViewHolder).binding) {
            val comment = guestComments[position]
            textGuestId.text = comment.guestUserID
            textGuestComment.text = comment.comment
            textEditDate.text = SimpleDateFormat("yyyy-MMM-dd HH:mm").format(Date(comment.createdTime))
            textButtonDelete.isVisible = false
            if (watchingUserID == comment.guestUserID || watchingUserID == comment.ownerUserID)
                textButtonDelete.isVisible = true
            textButtonDelete.setOnClickListener {
                deleteComment(position)
                GuestCommentRepository.deleteComment(comment)
            }
        }
    }

    fun addComment(guestComment: GuestCommentEntity) {
        GuestCommentRepository.addComment(guestComment)
        guestComments.add(0, guestComment)
        notifyItemInserted(0)
    }

    fun deleteComment(position: Int) {
        GuestCommentRepository.deleteComment(guestComments[position])
        guestComments.removeAt(position)
        notifyItemRemoved(position)
    }
    inner class GuestViewHolder(val binding: ItemGuestCommentBinding): RecyclerView.ViewHolder(binding.root)

}