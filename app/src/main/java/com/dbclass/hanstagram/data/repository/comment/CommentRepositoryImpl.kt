package com.dbclass.hanstagram.data.repository.comment

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.db.comments.CommentsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CommentRepositoryImpl : CommentRepository() {
    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.commentsDao()
        }
    }

    override suspend fun leaveComment(comment: CommentEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertComment(comment)
        }
    }

    override suspend fun deleteComment(comment: CommentEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.deleteComment(comment)
        }
    }

    override suspend fun updateComment(comment: CommentEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateComment(comment)
        }
    }

    override suspend fun getComments(postID: Long): List<CommentEntity> {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getComments(postID) ?: listOf()
        }
    }

    override suspend fun getCommentsCount(postID: Long): Long {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getCommentsCount(postID) ?: 0L
        }
    }
}