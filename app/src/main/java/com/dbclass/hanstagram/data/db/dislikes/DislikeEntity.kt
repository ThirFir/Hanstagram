package com.dbclass.hanstagram.data.db.dislikes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.users.UserEntity

@Entity(tableName = "dislikes", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = PostEntity::class,
        parentColumns = ["post_id"],
        childColumns = ["post_id"],
        onDelete = CASCADE
    )
])
data class DislikeEntity(
    @ColumnInfo(name = "user_id") val userID: String,
    @ColumnInfo(name = "post_id") val postID: Long,
    @PrimaryKey(autoGenerate = true) val pid: Long = 0
)