package com.dbclass.hanstagram.data.db.follows

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dbclass.hanstagram.data.db.users.UserEntity

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

    @Query("select * from users where id in (select follower_id from follows where following_id = :id)")
    fun getFollowers(id: String): List<UserEntity>?

    @Query("select * from users where id in (select following_id from follows where follower_id = :id)")
    fun getFollowings(id: String): List<UserEntity>?
}