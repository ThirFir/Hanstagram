package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.db.comments.CommentsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object CommentRepository {
    private var commentsDao: CommentsDao? = null

    fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            commentsDao = HanstagramDatabase.getInstance(appContext)?.commentsDao()
        }
    }

    fun leaveComment(comment: CommentEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            commentsDao?.insertComment(comment)
        }
    }

    fun deleteComment(comment: CommentEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            commentsDao?.deleteComment(comment)
        }
    }

    fun updateComment(comment: CommentEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            commentsDao?.updateComment(comment)
        }
    }

    suspend fun getComments(postID: Long): List<CommentEntity> {
        return CoroutineScope(Dispatchers.Default).async {
            commentsDao?.getComments(postID) ?: listOf()
        }.await()
    }
}