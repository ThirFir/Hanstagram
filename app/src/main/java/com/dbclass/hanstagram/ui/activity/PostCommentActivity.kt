package com.dbclass.hanstagram.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.CommentRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.databinding.ActivityPostCommentBinding
import com.dbclass.hanstagram.ui.adapter.PostCommentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dbclass.hanstagram.data.utils.closeKeyboard

class PostCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostCommentBinding
    private var userID: String? = null
    private var postID: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = intent.getStringExtra("user_id")
        postID = intent.getLongExtra("post_id", 0L)

        binding.postCommentToolbar.setTitle(R.string.special_app_name)
        setSupportActionBar(binding.postCommentToolbar)


        CoroutineScope(Dispatchers.Default).launch {
            val profileImage = UserRepository.getProfileImage(userID ?: return@launch)
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(this@PostCommentActivity).load(profileImage).error(R.drawable.baseline_account_circle_48).into(binding.imageProfile)
            }
        }

        binding.recyclerviewPostComments.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.Default).launch {
            val comments = CommentRepository.getComments(postID)

            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewPostComments.adapter =
                    PostCommentAdapter(comments as MutableList, postID, userID)
                binding.textButtonLeaveComment.setOnClickListener {
                    binding.editTextPostComment.text.ifEmpty {
                        Toast.makeText(this@PostCommentActivity, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
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
}