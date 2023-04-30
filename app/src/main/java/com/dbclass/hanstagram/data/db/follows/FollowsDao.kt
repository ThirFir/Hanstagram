package com.dbclass.hanstagram.data.db.follows

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FollowsDao {

    @Insert
    fun insertFollow(follow: FollowEntity)

    @Delete
    fun deleteFollow(follow: FollowEntity)

    @Query("select pid from follows where follower_id = :id")
    fun getFollowersCount(id: String): Int

    @Query("select pid from follows where follower_id = :id")
    fun getFollowingsCount(id: String): Int

    @Query("select pid from follows where follower_id = :follower and following_id = :following")
    fun getIsFollowing(follower: String, following: String) : Int
}