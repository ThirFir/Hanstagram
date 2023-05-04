package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.follows.FollowEntity
import com.dbclass.hanstagram.data.db.follows.FollowsDao
import kotlinx.coroutines.*

object FollowRepository {
    private var followsDao: FollowsDao? = null
    private val followScope = CoroutineScope(Dispatchers.Default)

    fun init(appContext: Context) {
        followsDao = HanstagramDatabase.getInstance(appContext)?.followsDao()
    }

    suspend fun getFollowPID(follower: String, following: String): Long? {
        return followScope.async {
            followsDao?.getFollowPID(followerID = follower, followingID = following)
        }.await()
    }

    /** return new pid */
    suspend fun doFollow(follower: String, following: String): Long? {
        return followScope.async {
            followsDao?.insertFollow(FollowEntity(followingID = following, followerID = follower))
            followsDao?.getFollowPID(followerID = follower, followingID = following)
        }.await()
    }

    fun doUnFollow(pid: Long) {
        followScope.launch {
            followsDao?.deleteFollow(pid)
        }
    }

    suspend fun getFollowersCount(userID: String): Long {
        return followScope.async {
            followsDao?.getFollowersCount(userID)
        }.await() ?: 0
    }
    suspend fun getFollowingsCount(userID: String): Long {
        return followScope.async {
            followsDao?.getFollowingsCount(userID)
        }.await() ?: 0
    }

    suspend fun getFollowings(userID: String): List<String> {
        return followScope.async {
            followsDao?.getFollowings(userID) ?: listOf()
        }.await()
    }

    suspend fun getFollowers(userID: String): List<String> {
        return followScope.async {
            followsDao?.getFollowers(userID) ?: listOf()
        }.await()
    }
}