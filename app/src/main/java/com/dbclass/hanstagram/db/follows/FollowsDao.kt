package com.dbclass.hanstagram.db.follows

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface FollowsDao {

    @Insert
    fun follow(follow: FollowEntity)

    @Delete
    fun unFollow(follow: FollowEntity)

}