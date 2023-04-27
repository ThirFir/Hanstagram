package com.dbclass.hanstagram.db.comments

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: Long = 0,
    val postID: Long,
    val userID: String,
    val content: String,
    val timestamp: Long
)