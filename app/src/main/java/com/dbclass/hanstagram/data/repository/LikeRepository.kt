package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.data.db.likes.LikesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object LikeRepository {
    private var likesDao: LikesDao? = null

    fun init(appContext: Context) {
        likesDao = HanstagramDatabase.getInstance(appContext)?.likesDao()
    }

    suspend fun doLike(like: LikeEntity): LikeEntity? {
        return CoroutineScope(Dispatchers.Default).async {
            likesDao?.insertLike(like)
            likesDao?.getLike(like.userID, like.postID)
        }.await()
    }

    fun cancelLike(pid: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            likesDao?.deleteLike(pid)
        }
    }

    suspend fun getLike(userID: String, postID: Long): LikeEntity? {
        return CoroutineScope(Dispatchers.Default).async {
            likesDao?.getLike(userID, postID)
        }.await()
    }

    suspend fun getLikesCount(postID: Long): Long {
        return CoroutineScope(Dispatchers.Default).async {
            likesDao?.getLikesCount(postID) ?: 0L
        }.await()
    }
}