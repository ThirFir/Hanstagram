package com.dbclass.hanstagram.data.repository.message

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.db.messages.MessagesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MessageRepositoryImpl: MessageRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.messagesDao()
        }
    }

    override suspend fun sendMessage(message: MessageEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.insertMessage(message)
        }
    }

    override suspend fun getHasUnreadMessage(userID: String): Boolean {
        return withContext(dbScope.coroutineContext) {
            dao?.getHasUnreadMessage(userID) ?: false
        }
    }


    override suspend fun getUnreadMessages(userID: String): List<MessageEntity> {
        return withContext(dbScope.coroutineContext) {
            dao?.getUnreadMessages(userID) ?: listOf()
        }
    }

    override suspend fun getReceivedMessages(userID: String): List<MessageEntity> {
        return withContext(dbScope.coroutineContext) {
            dao?.getReceivedMessages(userID) ?: listOf()
        }
    }

    override suspend fun getSentMessaged(userID: String): List<MessageEntity> {
        return withContext(dbScope.coroutineContext) {
            dao?.getSentMessages(userID) ?: listOf()
        }
    }

    override suspend fun setMessageReadStateTrue(pid: Long) {
        withContext(dbScope.coroutineContext) {
            dao?.setReadStateTrue(pid)
        }
    }
}