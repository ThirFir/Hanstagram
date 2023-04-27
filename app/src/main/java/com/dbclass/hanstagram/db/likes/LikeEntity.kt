package com.dbclass.hanstagram.db.likes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class LikeEntity(
    @PrimaryKey val id: Long = 0,
    val userID: String,
    val postID: Long,

)