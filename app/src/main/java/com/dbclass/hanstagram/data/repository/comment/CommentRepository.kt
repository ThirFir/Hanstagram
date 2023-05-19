package com.dbclass.hanstagram.data.repository.comment

import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.db.comments.CommentsDao
import com.dbclass.hanstagram.data.repository.Repository


abstract class CommentRepository: Repository {
    var dao: CommentsDao? = null

    abstract suspend fun leaveComment(comment: CommentEntity)
    abstract suspend fun deleteComment(comment: CommentEntity)
    abstract suspend fun updateComment(comment: CommentEntity)
    abstract suspend fun getComments(postID: Long): List<CommentEntity>
    abstract suspend fun getCommentsCount(postID: Long): Long
}