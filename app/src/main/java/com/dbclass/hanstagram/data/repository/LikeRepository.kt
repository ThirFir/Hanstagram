package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.data.db.likes.LikesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LikeRepository {
    private var likesDao: LikesDao? = null

    fun init(appContext: Context) {
        likesDao = HanstagramDatabase.getInstance(appContext)?.likesDao()
    }

    fun doLike(like: LikeEntity){
        CoroutineScope(Dispatchers.Default).launch {
            likesDao?.insertLike(like)
        }
    }

    fun cancelLike(pid: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            likesDao?.deleteLike(pid)
        }
    }
}