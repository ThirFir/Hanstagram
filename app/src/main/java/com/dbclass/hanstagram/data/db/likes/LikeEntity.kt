package com.dbclass.hanstagram.data.db.likes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.users.UserEntity

@Entity(tableName = "likes", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = PostEntity::class,
        parentColumns = ["post_id"],
        childColumns = ["post_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class LikeEntity(
    @ColumnInfo(name = "user_id") val userID: String,
    @ColumnInfo(name = "post_id") val postID: Long,
    @PrimaryKey val pid: Long = 0
)