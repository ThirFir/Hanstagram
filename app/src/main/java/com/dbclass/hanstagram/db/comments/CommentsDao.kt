package com.dbclass.hanstagram.db.comments

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface CommentsDao {

    @Insert
    fun leaveComment(comment: CommentEntity)

    @Delete
    fun deleteComment(comment: CommentEntity)

    @Update
    fun updateComment(comment: CommentEntity)
}