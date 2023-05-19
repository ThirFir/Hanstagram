package com.dbclass.hanstagram.data.db.comments

import androidx.room.*

@Dao
interface CommentsDao {

    @Insert
    suspend fun insertComment(comment: CommentEntity)

    @Delete
    suspend fun deleteComment(comment: CommentEntity)

    @Update
    suspend fun updateComment(comment: CommentEntity)

    @Query("select * from comments where post_id=:postID")
    suspend fun getComments(postID: Long): List<CommentEntity>

    @Query("SELECT count(pid) from comments where post_id = :postID")
    suspend fun getCommentsCount(postID: Long): Long
}