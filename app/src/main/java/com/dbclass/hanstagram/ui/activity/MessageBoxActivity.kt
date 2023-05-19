package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.repository.message.MessageRepository
import com.dbclass.hanstagram.data.repository.message.MessageRepositoryImpl
import com.dbclass.hanstagram.databinding.ActivityMessageBoxBinding
import com.dbclass.hanstagram.ui.adapter.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageBoxActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBoxBinding
    private lateinit var userID: String
    private var receivedMessages = listOf<MessageEntity>()
    private var sentMessages = listOf<MessageEntity>()
    private var unreadMessages = listOf<MessageEntity>()
    private val messageRepository: MessageRepository = MessageRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            userID = intent.getStringExtra("user_id")!!
        } catch (e: Exception) {
            Log.d("MessageBoxActivity", "User ID Load Failure")
            finish()
        }

        binding.messageBoxToolbar.setTitle(R.string.special_app_name)
        setSupportActionBar(binding.messageBoxToolbar)

        binding.textReceivedMessage.isSelected = true
        binding.recyclerviewMessageBox.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.Default).launch {
            receivedMessages = messageRepository.getReceivedMessages(userID) as MutableList
            sentMessages = messageRepository.getSentMessaged(userID) as MutableList
            unreadMessages = messageRepository.getUnreadMessages(userID) as MutableList

            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewMessageBox.adapter = MessageAdapter(receivedMessages as MutableList, userID)
                binding.textReceivedMessage.setOnClickListener {
                    (binding.recyclerviewMessageBox.adapter as MessageAdapter).updateMessages(receivedMessages)
                    binding.textReceivedMessage.isSelected = true
                    binding.textSentMessage.isSelected = false
                }
                /*binding.textUnreadMessage.setOnClickListener {
                    (binding.recyclerviewMessageBox.adapter as MessageAdapter).updateMessages(unreadMessages)
                }*/
                binding.textSentMessage.setOnClickListener {
                    (binding.recyclerviewMessageBox.adapter as MessageAdapter).updateMessages(sentMessages)
                    binding.textReceivedMessage.isSelected = false
                    binding.textSentMessage.isSelected = true
                }
            }
        }
    }
}