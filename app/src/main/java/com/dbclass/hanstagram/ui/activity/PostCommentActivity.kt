package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.comment.CommentRepository
import com.dbclass.hanstagram.data.repository.comment.CommentRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.databinding.ActivityPostCommentBinding
import com.dbclass.hanstagram.ui.adapter.PostCommentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dbclass.hanstagram.data.utils.closeKeyboard
import kotlinx.coroutines.CoroutineDispatcher

class PostCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostCommentBinding
    private val commentRepository: CommentRepository = CommentRepositoryImpl
    private val userRepository: UserRepository = UserRepositoryImpl
    private var userID: String? = null
    private var postID: Long = 0L
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = intent.getStringExtra("user_id")
        postID = intent.getLongExtra("post_id", 0L)

        binding.postCommentToolbar.setTitle(R.string.special_app_name)
        setSupportActionBar(binding.postCommentToolbar)


        uiScope.launch {
            val profileImage = userRepository.getProfileImage(userID ?: return@launch)
            Glide.with(this@PostCommentActivity).load(profileImage).error(R.drawable.ic_account_96)
                .into(binding.imageProfile)

        }

        binding.recyclerviewPostComments.layoutManager = LinearLayoutManager(this)
        uiScope.launch {
            val comments = commentRepository.getComments(postID)

            binding.recyclerviewPostComments.adapter =
                PostCommentAdapter(comments as MutableList, postID, userID)
            binding.textButtonLeaveComment.setOnClickListener {
                binding.editTextPostComment.text.ifEmpty {
                    Toast.makeText(this@PostCommentActivity, "댓글을 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                this@PostCommentActivity.closeKeyboard(binding.editTextPostComment)
                (binding.recyclerviewPostComments.adapter as PostCommentAdapter).addComment(
                    userID ?: return@setOnClickListener,
                    binding.editTextPostComment.text.toString()
                )
                binding.editTextPostComment.setText("")
            }
        }

    }
}