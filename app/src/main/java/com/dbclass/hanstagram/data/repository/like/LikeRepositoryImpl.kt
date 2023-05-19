package com.dbclass.hanstagram.data.repository.like

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.data.db.likes.LikesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object LikeRepositoryImpl : LikeRepository() {

    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.likesDao()
        }
    }

    override suspend fun doLike(like: LikeEntity): LikeEntity? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertLike(like)
            dao?.getLike(like.userID, like.postID)
        }
    }

    override suspend fun cancelLike(pid: Long) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.deleteLike(pid)
        }
    }

    override suspend fun getLike(userID: String, postID: Long): LikeEntity? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getLike(userID, postID)
        }
    }

    override suspend fun getLikesCount(postID: Long): Long {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getLikesCount(postID) ?: 0L
        }
    }
}