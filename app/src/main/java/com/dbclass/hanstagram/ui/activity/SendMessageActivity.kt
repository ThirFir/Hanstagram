package com.dbclass.hanstagram.ui.activity

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.repository.message.MessageRepository
import com.dbclass.hanstagram.data.repository.message.MessageRepositoryImpl
import com.dbclass.hanstagram.data.utils.StringConstants.FROM_ID
import com.dbclass.hanstagram.data.utils.StringConstants.TO_ID
import com.dbclass.hanstagram.data.utils.closeKeyboard
import com.dbclass.hanstagram.databinding.ActivitySendMessageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendMessageBinding
    private val messageRepository: MessageRepository = MessageRepositoryImpl
    private val mainDispatcher = Dispatchers.Main
    private val uiScope = CoroutineScope(mainDispatcher)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toID = intent.getStringExtra(TO_ID)
        val fromID = intent.getStringExtra(FROM_ID)
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
                uiScope.launch {
                    messageRepository.sendMessage(
                        MessageEntity(
                            fromID,
                            toID,
                            false,
                            content,
                            System.currentTimeMillis()
                        )
                    )
                    NotificationCompat.Builder(this@SendMessageActivity, "MESSAGE_CHANNEL")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("${fromID}님의 메세지")
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build()

                    Toast.makeText(this@SendMessageActivity, "${toID}에게 메세지를 보냈습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }
    }
}