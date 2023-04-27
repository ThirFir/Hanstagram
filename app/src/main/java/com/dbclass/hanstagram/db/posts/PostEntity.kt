package com.dbclass.hanstagram.db.posts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long = 0,
    val userID: String,
    var caption: String,
    var images: String,
    val timestamp: Long

)
