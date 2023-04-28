package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.follows.FollowEntity
import com.dbclass.hanstagram.data.db.follows.FollowsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FollowRepository {
    private var followsDao: FollowsDao? = null

    fun init(appContext: Context) {
        followsDao = HanstagramDatabase.getInstance(appContext)?.followsDao()
    }

    fun doFollow(follow: FollowEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            followsDao?.insertFollow(follow)
        }
    }

    fun doUnFollow(follow: FollowEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            followsDao?.deleteFollow(follow)
        }
    }
}