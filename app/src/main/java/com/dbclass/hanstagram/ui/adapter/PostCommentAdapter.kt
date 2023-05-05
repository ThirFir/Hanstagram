package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.repository.CommentRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.databinding.ItemPostCommentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostCommentAdapter(private val postComments: MutableList<CommentEntity>, val postID: Long, val userID: String?, val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = PostCommentViewHolder(ItemPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = postComments.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postComment = postComments[position]
        with((holder as PostCommentViewHolder).binding){
            textGuestComment.text = postComment.content
            textGuestId.text = postComment.userID
            textEditDate.text = postComment.createdTime.toString()

            if(userID != null && postComment.userID != userID)
                textButtonDelete.isVisible = false
            textButtonDelete.setOnClickListener {
                deleteComment(position)
            }
            CoroutineScope(Dispatchers.Default).launch {
                val image = UserRepository.getProfileImage(postComment.userID)
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(context).load(image).error(R.drawable.baseline_account_circle_48).into(imageProfile)
                }
            }
        }
    }

    fun addComment(userID: String, content: String) {
        val newComment = CommentEntity(postID, userID, content, System.currentTimeMillis())
        CommentRepository.leaveComment(newComment)
        postComments.add(newComment)
        notifyItemInserted(postComments.size - 1)
    }

    private fun deleteComment(pos: Int) {
        CommentRepository.deleteComment(postComments[pos])
        postComments.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, itemCount)
    }

    inner class PostCommentViewHolder(val binding: ItemPostCommentBinding): RecyclerView.ViewHolder(binding.root)
}