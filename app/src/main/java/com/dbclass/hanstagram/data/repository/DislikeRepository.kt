package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.dislikes.DislikeEntity
import com.dbclass.hanstagram.data.db.dislikes.DislikesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DislikeRepository {
    private var dislikesDao: DislikesDao? = null

    fun init(appContext: Context) {
        dislikesDao = HanstagramDatabase.getInstance(appContext)?.dislikesDao()
    }

    fun doDislike(dislike: DislikeEntity){
        CoroutineScope(Dispatchers.Default).launch {
            dislikesDao?.insertDislike(dislike)
        }
    }

    fun cancelDislike(dislike: DislikeEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            dislikesDao?.deleteDislike(dislike)
        }
    }
}