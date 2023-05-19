package com.dbclass.hanstagram.data.repository.follow

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.follows.FollowEntity
import com.dbclass.hanstagram.data.db.follows.FollowsDao
import com.dbclass.hanstagram.data.db.users.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object FollowRepositoryImpl : FollowRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.followsDao()
        }
    }

    override suspend fun getFollowPID(follower: String, following: String): Long? {
        return withContext(dbScope.coroutineContext) {
            dao?.getFollowPID(followerID = follower, followingID = following)
        }
    }

    /** return new pid */
    override suspend fun doFollow(follower: String, following: String): Long? {
        return withContext(dbScope.coroutineContext) {
            dao?.insertFollow(FollowEntity(followingID = following, followerID = follower))
            dao?.getFollowPID(followerID = follower, followingID = following)
        }
    }

    override suspend fun doUnFollow(pid: Long) {
        withContext(dbScope.coroutineContext) {
            dao?.deleteFollow(pid)
        }
    }

    override suspend fun doUnFollow(follower: String, following: String) {
        withContext(dbScope.coroutineContext) {
            dao?.deleteFollow(follower, following)
        }
    }

    override suspend fun getFollowersCount(userID: String): Long {
        return withContext(dbScope.coroutineContext) {
            dao?.getFollowersCount(userID)
        } ?: 0
    }
    override suspend fun getFollowingsCount(userID: String): Long {
        return withContext(dbScope.coroutineContext) {
            dao?.getFollowingsCount(userID)
        } ?: 0
    }

    override suspend fun getFollowings(userID: String?): List<UserEntity> {
        return if (userID != null) withContext(dbScope.coroutineContext) {
            dao?.getFollowings(userID) ?: listOf()
        }
        else listOf()
    }

    override suspend fun getFollowers(userID: String?): List<UserEntity> {
        return if (userID != null) withContext(dbScope.coroutineContext) {
            dao?.getFollowers(userID) ?: listOf()
        }
        else listOf()
    }
}