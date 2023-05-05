package com.dbclass.hanstagram.data.db.comments

import androidx.room.*

@Dao
interface CommentsDao {

    @Insert
    fun insertComment(comment: CommentEntity)

    @Delete
    fun deleteComment(comment: CommentEntity)

    @Update
    fun updateComment(comment: CommentEntity)

    @Query("select * from comments where post_id=:postID")
    fun getComments(postID: Long): List<CommentEntity>
}