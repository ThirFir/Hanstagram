package com.dbclass.hanstagram.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.ActivityLoginBinding
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
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
                CoroutineScope(Dispatchers.Default).launch {
                    val db = HanstagramDatabase.getInstance(this@LoginActivity)
                    val user = db?.usersDao()?.getUser(id)
                    if (user == null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@LoginActivity, R.string.toast_no_exist_id, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if(id == user.id && password == user.password) { // Login Success
                            CoroutineScope(Dispatchers.Main).launch {
                                userViewModel.setUser(user)
                                val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(mainIntent)
                                finish()
                            }
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
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
        }
        binding.textRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }

    private fun initializeVariables() {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        CoroutineScope(Dispatchers.Default).launch {
            with(applicationContext) {
                UserRepository.init(this)
                PostRepository.init(this)
                LikeRepository.init(this)
                FollowRepository.init(this)
                DislikeRepository.init(this)
                CommentRepository.init(this)
            }
        }
    }
}