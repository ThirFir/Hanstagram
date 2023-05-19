package com.dbclass.hanstagram.data.repository.comment

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.db.comments.CommentsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CommentRepositoryImpl : CommentRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.commentsDao()
        }
    }

    override suspend fun leaveComment(comment: CommentEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.insertComment(comment)
        }
    }

    override suspend fun deleteComment(comment: CommentEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.deleteComment(comment)
        }
    }

    override suspend fun updateComment(comment: CommentEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.updateComment(comment)
        }
    }

    override suspend fun getComments(postID: Long): List<CommentEntity> {
        return withContext(dbScope.coroutineContext) {
            dao?.getComments(postID) ?: listOf()
        }
    }

    override suspend fun getCommentsCount(postID: Long): Long {
        return withContext(dbScope.coroutineContext) {
            dao?.getCommentsCount(postID) ?: 0L
        }
    }
}