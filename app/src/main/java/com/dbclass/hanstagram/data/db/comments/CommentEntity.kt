package com.dbclass.hanstagram.data.db.comments

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.users.UserEntity

@Entity(tableName = "comments", foreignKeys = [
    ForeignKey(
        entity = PostEntity::class,
        parentColumns = ["post_id"],
        childColumns = ["post_id"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = CASCADE
    )
])
data class CommentEntity(
    @ColumnInfo(name = "post_id") val postID: Long,
    @ColumnInfo(name = "user_id") val userID: String,
    val content: String,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    @PrimaryKey(autoGenerate = true) val pid: Long = 0
)