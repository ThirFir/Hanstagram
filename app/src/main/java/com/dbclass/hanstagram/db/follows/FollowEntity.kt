package com.dbclass.hanstagram.db.follows

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey val id: Long = 0,
    val followerID: String,
    val followingID: String
)