package com.dbclass.hanstagram.data.repository.like

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.data.db.likes.LikesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object LikeRepositoryImpl : LikeRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.likesDao()
        }
    }

    override suspend fun doLike(like: LikeEntity): LikeEntity? {
        return withContext(dbScope.coroutineContext) {
            dao?.insertLike(like)
            dao?.getLike(like.userID, like.postID)
        }
    }

    override suspend fun cancelLike(pid: Long) {
        withContext(dbScope.coroutineContext) {
            dao?.deleteLike(pid)
        }
    }

    override suspend fun getLike(userID: String, postID: Long): LikeEntity? {
        return withContext(dbScope.coroutineContext) {
            dao?.getLike(userID, postID)
        }
    }

    override suspend fun getLikesCount(postID: Long): Long {
        return withContext(dbScope.coroutineContext) {
            dao?.getLikesCount(postID) ?: 0L
        }
    }
}