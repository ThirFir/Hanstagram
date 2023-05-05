package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.db.messages.MessagesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object MessageRepository {
    private var messagesDao: MessagesDao? = null

    fun init(appContext: Context) {
        messagesDao = HanstagramDatabase.getInstance(appContext)?.messagesDao()
    }

    fun sendMessage(message: MessageEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            messagesDao?.insertMessage(message)
        }
    }

    suspend fun getHasUnreadMessage(userID: String): Boolean {
        return CoroutineScope(Dispatchers.Default).async {
            messagesDao?.getHasUnreadMessage(userID) ?: false
        }.await()
    }


    suspend fun getUnreadMessages(userID: String): List<MessageEntity> {
        return CoroutineScope(Dispatchers.Default).async {
            messagesDao?.getUnreadMessages(userID) ?: listOf()
        }.await()
    }

    suspend fun getReceivedMessages(userID: String): List<MessageEntity> {
        return CoroutineScope(Dispatchers.Default).async {
            messagesDao?.getReceivedMessages(userID) ?: listOf()
        }.await()
    }

    suspend fun getSentMessaged(userID: String): List<MessageEntity> {
        return CoroutineScope(Dispatchers.Default).async {
            messagesDao?.getSentMessages(userID) ?: listOf()
        }.await()
    }

    fun setMessageReadStateTrue(pid: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            messagesDao?.setReadStateTrue(pid)
        }
    }
}