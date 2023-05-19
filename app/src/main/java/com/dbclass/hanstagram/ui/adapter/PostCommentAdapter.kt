package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.repository.comment.CommentRepository
import com.dbclass.hanstagram.data.repository.comment.CommentRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.data.utils.getFormattedDate
import com.dbclass.hanstagram.data.utils.showKeyboard
import com.dbclass.hanstagram.databinding.ItemPostCommentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostCommentAdapter(private val postComments: MutableList<CommentEntity>, val postID: Long, val userID: String?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private val commentRepository: CommentRepository = CommentRepositoryImpl
    private val userRepository: UserRepository = UserRepositoryImpl
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return PostCommentViewHolder(ItemPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = postComments.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postComment = postComments[position]
        with((holder as PostCommentViewHolder).binding){
            textGuestComment.text = postComment.content
            textGuestId.text = postComment.userID
            textEditDate.text = getFormattedDate(postComment.createdTime)

            editTextComment.isVisible = false
            buttonEditComment.isVisible = false

            if(userID != null && postComment.userID != userID) {
                textButtonDelete.isVisible = false
                textButtonEdit.isVisible = false
            }

            textButtonEdit.setOnClickListener {
                textGuestComment.isVisible = false
                textEditDate.isVisible = false
                textButtonDelete.isVisible = false
                editTextComment.isVisible = true
                buttonEditComment.isVisible = true

                editTextComment.setText(textGuestComment.text)
                editTextComment.requestFocus()
                context.showKeyboard(editTextComment)

                buttonEditComment.setOnClickListener {
                    val newContent = editTextComment.text
                    editComment(position, newContent)

                    buttonEditComment.isVisible = false
                    editTextComment.isVisible = false
                    textGuestComment.isVisible = true
                    textEditDate.isVisible = true
                    textButtonDelete.isVisible = true
                }
            }

            textButtonDelete.setOnClickListener {
                deleteComment(position)
            }
            CoroutineScope(Dispatchers.Main).launch {
                val image = userRepository.getProfileImage(postComment.userID)
                Glide.with(context).load(image).error(R.drawable.ic_account_96).into(imageProfile)
            }
        }
    }

    fun addComment(userID: String, content: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val newComment = CommentEntity(postID, userID, content, System.currentTimeMillis())
            commentRepository.leaveComment(newComment)
            postComments.add(newComment)
            notifyItemInserted(postComments.size - 1)
        }
    }

    private fun deleteComment(pos: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            commentRepository.deleteComment(postComments[pos])
            postComments.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, itemCount)
        }
    }
    private fun editComment(pos: Int, content: Editable) {
        CoroutineScope(Dispatchers.Main).launch {
            val editedComment = CommentEntity(
                postID,
                postComments[pos].userID,
                content.toString(),
                postComments[pos].createdTime,
                postComments[pos].pid
            )
            commentRepository.updateComment(editedComment)
            postComments[pos] = editedComment
            notifyItemChanged(pos)
        }
    }

    inner class PostCommentViewHolder(val binding: ItemPostCommentBinding): RecyclerView.ViewHolder(binding.root)
}