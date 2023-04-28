package com.dbclass.hanstagram.data.db.posts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @ColumnInfo(name = "user_id") val userID: String,
    var caption: String?,
    var images: String,
    @ColumnInfo(name = "created_time") val createdTime: Long
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "post_id") val postID: Long = 0
}
