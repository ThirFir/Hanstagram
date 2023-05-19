package com.dbclass.hanstagram.data.repository.message

import com.dbclass.hanstagram.data.db.messages.MessageEntity
import com.dbclass.hanstagram.data.db.messages.MessagesDao
import com.dbclass.hanstagram.data.repository.Repository

abstract class MessageRepository : Repository {
    var dao: MessagesDao? = null

    abstract suspend fun sendMessage(message: MessageEntity)
    abstract suspend fun getHasUnreadMessage(userID: String): Boolean
    abstract suspend fun getUnreadMessages(userID: String): List<MessageEntity>
    abstract suspend fun getReceivedMessages(userID: String): List<MessageEntity>
    abstract suspend fun getSentMessaged(userID: String): List<MessageEntity>
    abstract suspend fun setMessageReadStateTrue(pid: Long)
}