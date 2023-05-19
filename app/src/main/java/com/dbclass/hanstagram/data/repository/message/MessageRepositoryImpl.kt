package com.dbclass.hanstagram.data.repository.message

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.db.messages.MessagesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MessageRepositoryImpl: MessageRepository() {

    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.messagesDao()
        }
    }

    override suspend fun sendMessage(message: MessageEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertMessage(message)
        }
    }

    override suspend fun getHasUnreadMessage(userID: String): Boolean {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getHasUnreadMessage(userID) ?: false
        }
    }


    override suspend fun getUnreadMessages(userID: String): List<MessageEntity> {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getUnreadMessages(userID) ?: listOf()
        }
    }

    override suspend fun getReceivedMessages(userID: String): List<MessageEntity> {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getReceivedMessages(userID) ?: listOf()
        }
    }

    override suspend fun getSentMessaged(userID: String): List<MessageEntity> {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getSentMessages(userID) ?: listOf()
        }
    }

    override suspend fun setMessageReadStateTrue(pid: Long) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.setReadStateTrue(pid)
        }
    }
}