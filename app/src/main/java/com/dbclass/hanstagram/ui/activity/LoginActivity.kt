package com.dbclass.hanstagram.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.ActivityLoginBinding
import com.dbclass.hanstagram.data.repository.comment.CommentRepositoryImpl
import com.dbclass.hanstagram.data.repository.dislike.DislikeRepositoryImpl
import com.dbclass.hanstagram.data.repository.follow.FollowRepositoryImpl
import com.dbclass.hanstagram.data.repository.guest.GuestCommentRepositoryImpl
import com.dbclass.hanstagram.data.repository.like.LikeRepositoryImpl
import com.dbclass.hanstagram.data.repository.message.MessageRepositoryImpl
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val userRepository: UserRepository = UserRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeVariables()

        binding.buttonLogin.setOnClickListener {
            val id = binding.editTextId.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (id.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_id, Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, R.string.toast_input_password, Toast.LENGTH_SHORT).show()
            } else {
                // 로그인 시도 (내부 DB)
                CoroutineScope(Dispatchers.Main).launch {
                    val user = userRepository.getUser(id)
                    if (user == null) {
                        Toast.makeText(
                            this@LoginActivity,
                            R.string.toast_no_exist_id,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        if (id == user.id && password == user.password) { // Login Success
                            val mainIntent =
                                Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    putExtra("user_id", id)
                                }
                            startActivity(mainIntent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                R.string.toast_wrong_account,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        binding.textRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }

    private fun initializeVariables() {
        applicationContext.run {
            UserRepositoryImpl.init(this)
            PostRepositoryImpl.init(this)
            LikeRepositoryImpl.init(this)
            FollowRepositoryImpl.init(this)
            DislikeRepositoryImpl.init(this)
            CommentRepositoryImpl.init(this)
            GuestCommentRepositoryImpl.init(this)
            MessageRepositoryImpl.init(this)
        }
    }
}