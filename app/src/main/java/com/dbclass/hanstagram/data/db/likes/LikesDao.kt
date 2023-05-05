package com.dbclass.hanstagram.data.db.likes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikesDao {

    @Insert
    fun insertLike(like: LikeEntity)

    @Query("delete from likes where pid = :pid")
    fun deleteLike(pid: Long)

    @Query("select * from likes ")
    fun getLike(): LikeEntity


}