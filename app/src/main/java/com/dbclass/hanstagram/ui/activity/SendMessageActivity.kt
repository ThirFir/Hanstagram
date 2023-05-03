package com.dbclass.hanstagram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.repository.MessageRepository
import com.dbclass.hanstagram.databinding.ActivitySendMessageBinding

class SendMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toID = intent.getStringExtra("to_id")
        val fromID = intent.getStringExtra("from_id")
        if(toID == null || fromID == null)
            finish()
        toID!!; fromID!!

        binding.textToId.text = toID
        binding.buttonSend.setOnClickListener {
            binding.editTextMessageContent.text.ifEmpty {
                Toast.makeText(this, R.string.toast_input_content, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val content = binding.editTextMessageContent.text.toString()
            MessageRepository.sendMessage(MessageEntity(fromID, toID, false, content, System.currentTimeMillis()))
        }
    }
}