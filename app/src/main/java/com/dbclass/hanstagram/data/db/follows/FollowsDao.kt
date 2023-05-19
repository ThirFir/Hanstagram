package com.dbclass.hanstagram.data.db.follows

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dbclass.hanstagram.data.db.users.UserEntity

@Dao
interface FollowsDao {

    @Insert
    suspend fun insertFollow(follow: FollowEntity)

    @Query("delete from follows where pid = :pid")
    suspend fun deleteFollow(pid: Long)

    @Query("select pid from follows where follower_id=:followerID and following_id=:followingID")
    suspend fun getFollowPID(followerID: String, followingID: String): Long?

    @Query("SELECT count(pid) from follows where following_id = :id")
    suspend fun getFollowersCount(id: String): Long

    @Query("SELECT count(pid) from follows where follower_id = :id")
    suspend fun getFollowingsCount(id: String): Long

    @Query("select pid from follows where follower_id = :follower and following_id = :following")
    suspend fun getIsFollowing(follower: String, following: String) : Int

    @Query("select * from users where id in (select follower_id from follows where following_id = :id)")
    suspend fun getFollowers(id: String): List<UserEntity>?

    @Query("select * from users where id in (select following_id from follows where follower_id = :id)")
    suspend fun getFollowings(id: String): List<UserEntity>?
}