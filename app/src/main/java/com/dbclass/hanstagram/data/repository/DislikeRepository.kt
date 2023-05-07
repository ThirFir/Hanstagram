package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.dislikes.DislikeEntity
import com.dbclass.hanstagram.data.db.dislikes.DislikesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object DislikeRepository {
    private var dislikesDao: DislikesDao? = null

    fun init(appContext: Context) {
        dislikesDao = HanstagramDatabase.getInstance(appContext)?.dislikesDao()
    }

    suspend fun doDislike(dislike: DislikeEntity): DislikeEntity? {
        return CoroutineScope(Dispatchers.Default).async {
            dislikesDao?.insertDislike(dislike)
            dislikesDao?.getDislike(dislike.userID, dislike.postID)
        }.await()
    }

    fun cancelDislike(pid: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            dislikesDao?.deleteDislike(pid)
        }
    }

    suspend fun getDislikesCount(postID: Long): Long {
        return CoroutineScope(Dispatchers.Default).async {
            dislikesDao?.getDislikesCount(postID) ?: 0L
        }.await()
    }

    suspend fun getDislike(userID: String, postID: Long): DislikeEntity? {
        return CoroutineScope(Dispatchers.Default).async {
            dislikesDao?.getDislike(userID, postID)
        }.await()
    }
}