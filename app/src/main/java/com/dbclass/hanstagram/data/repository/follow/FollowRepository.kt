package com.dbclass.hanstagram.data.repository.follow

import com.dbclass.hanstagram.data.db.follows.FollowsDao
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.Repository

abstract class FollowRepository: Repository {
    var dao: FollowsDao? = null

    abstract suspend fun getFollowPID(follower: String, following: String): Long?
    abstract suspend fun doFollow(follower: String, following: String): Long?
    abstract suspend fun doUnFollow(pid: Long)
    abstract suspend fun getFollowersCount(userID: String): Long
    abstract suspend fun getFollowingsCount(userID: String): Long
    abstract suspend fun getFollowings(userID: String?): List<UserEntity>
    abstract suspend fun getFollowers(userID: String?): List<UserEntity>
}