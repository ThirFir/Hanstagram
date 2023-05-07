package com.dbclass.hanstagram.data.db.dislikes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dbclass.hanstagram.data.db.likes.LikeEntity

@Dao
interface DislikesDao {

    @Insert
    fun insertDislike(dislike: DislikeEntity)

    @Query("delete from dislikes where pid=:pid")
    fun deleteDislike(pid: Long)

    @Query("select * from dislikes where user_id=:userID and post_id=:postID")
    fun getDislike(userID: String, postID: Long): DislikeEntity
    @Query("SELECT count(pid) from dislikes where post_id = :postID")
    fun getDislikesCount(postID: Long): Long
}