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

    @Query("")
    fun getFollowersCount(id: String)

    @Query("")
    fun getFollowingsCount(id: String)
}