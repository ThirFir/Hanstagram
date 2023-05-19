package com.dbclass.hanstagram.data.db.likes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikesDao {
    @Insert
    suspend fun insertLike(like: LikeEntity)

    @Query("delete from likes where pid = :pid")
    suspend fun deleteLike(pid: Long)

    @Query("select * from likes where user_id=:userID and post_id=:postID")
    suspend fun getLike(userID: String, postID: Long): LikeEntity

    @Query("SELECT count(pid) from likes where post_id = :postID")
    suspend fun getLikesCount(postID: Long): Long
}