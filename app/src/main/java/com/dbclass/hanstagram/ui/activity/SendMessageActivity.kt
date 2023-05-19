package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.repository.message.MessageRepository
import com.dbclass.hanstagram.data.repository.message.MessageRepositoryImpl
import com.dbclass.hanstagram.data.utils.closeKeyboard
import com.dbclass.hanstagram.databinding.ActivitySendMessageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendMessageBinding
    private val messageRepository: MessageRepository = MessageRepositoryImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toID = intent.getStringExtra("to_id")
        val fromID = intent.getStringExtra("from_id")
        if(toID == null || fromID == null) {
            Toast.makeText(this, "유저ID 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.textToId.text = "To - $toID"
        binding.buttonSend.setOnClickListener {
            binding.editTextMessageContent.text.ifEmpty {
                Toast.makeText(this, R.string.toast_input_content, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val content = binding.editTextMessageContent.text.toString()
            closeKeyboard(binding.editTextMessageContent)
            if(fromID != null && toID != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    messageRepository.sendMessage(
                        MessageEntity(
                            fromID,
                            toID,
                            false,
                            content,
                            System.currentTimeMillis()
                        )
                    )
                    Toast.makeText(this@SendMessageActivity, "${toID}에게 메세지를 보냈습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }
    }
}