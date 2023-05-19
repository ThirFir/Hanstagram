package com.dbclass.hanstagram.data.db.messages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessagesDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("select * from messages where to_user_id = :userID and is_read=false order by created_time desc")
    suspend fun getUnreadMessages(userID: String): List<MessageEntity>

    @Query("SELECT COUNT(is_read) from messages where to_user_id = :userID AND is_read = 0")
    suspend fun getHasUnreadMessage(userID: String): Boolean

    @Query("select * from messages where to_user_id=:userID order by created_time desc")
    suspend fun getReceivedMessages(userID: String) : List<MessageEntity>

    @Query("select * from messages where from_user_id=:userID order by created_time desc")
    suspend fun getSentMessages(userID: String): List<MessageEntity>

    @Query("update messages set is_read=true where pid=:pid")
    suspend fun setReadStateTrue(pid: Long)
}
