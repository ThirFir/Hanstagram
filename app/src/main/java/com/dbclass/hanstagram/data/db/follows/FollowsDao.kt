package com.dbclass.hanstagram.data.db.follows

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FollowsDao {

    @Insert
    fun insertFollow(follow: FollowEntity)

    @Query("delete from follows where pid = :pid")
    fun deleteFollow(pid: Long)

    @Query("select pid from follows where follower_id=:followerID and following_id=:followingID")
    fun getFollowPID(followerID: String, followingID: String): Long?

    @Query("SELECT count(pid) from follows where following_id = :id")
    fun getFollowersCount(id: String): Long

    @Query("SELECT count(pid) from follows where follower_id = :id")
    fun getFollowingsCount(id: String): Long

    @Query("select pid from follows where follower_id = :follower and following_id = :following")
    fun getIsFollowing(follower: String, following: String) : Int

    @Query("select following_id from follows where follower_id = :id")
    fun getFollowings(id: String): List<String>?   // id가 팔로우 중인 사람 return

    @Query("select follower_id from follows where following_id = :id")    // id를 팔로우 중인 사람 return
    fun getFollowers(id: String): List<String>?
}