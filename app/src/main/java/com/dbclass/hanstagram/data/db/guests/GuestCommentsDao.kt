package com.dbclass.hanstagram.data.db.guests

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GuestCommentsDao {
    @Insert
    suspend fun insertGuest(guestComment: GuestCommentEntity)

    @Delete
    suspend fun deleteGuest(guestComment: GuestCommentEntity)

    @Query("select * from guest_book where owner_user_id = :ownerUserID order by created_time desc")
    suspend fun getGuestComments(ownerUserID: String) : List<GuestCommentEntity>

}