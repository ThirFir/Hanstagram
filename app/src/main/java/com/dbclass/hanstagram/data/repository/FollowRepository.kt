package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.follows.FollowEntity
import com.dbclass.hanstagram.data.db.follows.FollowsDao
import kotlinx.coroutines.*

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

    suspend fun isFollowing(follower: String, following: String): Boolean {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            followsDao?.getIsFollowing(follower, following) != 0
        }
    }

    suspend fun getFollowersCount(userID: String): Int {
        return CoroutineScope(Dispatchers.Default).async {
            followsDao?.getFollowersCount(userID)
        }.await() ?: 0
    }
    suspend fun getFollowingsCount(userID: String): Int {
        return CoroutineScope(Dispatchers.Default).async {
            followsDao?.getFollowingsCount(userID)
        }.await() ?: 0
    }
}