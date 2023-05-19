package com.dbclass.hanstagram.data.repository.dislike

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.dislikes.DislikeEntity
import com.dbclass.hanstagram.data.db.dislikes.DislikesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DislikeRepositoryImpl : DislikeRepository() {

    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.dislikesDao()
        }
    }

    override suspend fun doDislike(dislike: DislikeEntity): DislikeEntity? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertDislike(dislike)
            dao?.getDislike(dislike.userID, dislike.postID)
        }
    }

    override suspend fun cancelDislike(pid: Long) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.deleteDislike(pid)
        }
    }

    override suspend fun getDislikesCount(postID: Long): Long {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getDislikesCount(postID) ?: 0L
        }
    }

    override suspend fun getDislike(userID: String, postID: Long): DislikeEntity? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getDislike(userID, postID)
        }
    }
}