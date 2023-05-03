package com.dbclass.hanstagram.data.db.messages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessagesDao {

    @Insert
    fun insertMessage(message: MessageEntity)

    @Delete
    fun deleteMessage(message: MessageEntity)

    @Query("select * from messages where to_user_id = :userID and is_read=false")
    fun getUnreadMessages(userID: String): List<MessageEntity>

    @Query("select * from messages where to_user_id = :userID")
    fun getAllOfUser(userID: String): List<MessageEntity>

    @Query("SELECT COUNT(is_read) from messages where to_user_id = :userID AND is_read = 0")
    fun getHasUnreadMessage(userID: String): Boolean
}