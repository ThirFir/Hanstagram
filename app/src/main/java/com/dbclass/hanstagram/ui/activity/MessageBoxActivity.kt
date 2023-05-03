package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.repository.MessageRepository
import com.dbclass.hanstagram.databinding.ActivityMessageBoxBinding
import com.dbclass.hanstagram.ui.adapter.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageBoxActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBoxBinding
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userID = intent.getStringExtra("user_id")
        if(userID == null){
            Log.d("MessageBoxActivity", "유저 ID 로드 실패")
            finish()
        }

        binding.messageBoxToolbar.setTitle(R.string.special_app_name)
        setSupportActionBar(binding.messageBoxToolbar)

        binding.recyclerviewMessageBox.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.Default).launch {
            val messages = MessageRepository.getAllMessages(userID!!) as MutableList
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewMessageBox.adapter = MessageAdapter(messages)
            }
        }

    }
}