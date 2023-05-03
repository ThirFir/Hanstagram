package com.dbclass.hanstagram.data.db.posts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.users.UserEntity

@Entity(tableName = "posts", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class PostEntity(
    @ColumnInfo(name = "user_id") val userID: String,
    var content: String?,
    var images: String,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "post_id") val postID: Long = 0L
)