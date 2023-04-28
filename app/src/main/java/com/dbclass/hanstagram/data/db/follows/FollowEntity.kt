package com.dbclass.hanstagram.data.db.follows

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.users.UserEntity

@Entity(tableName = "follows", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["follower_id"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["following_id"],
        onDelete = CASCADE
    )
])
data class FollowEntity(
    @ColumnInfo(name = "follower_id") val followerID: String,
    @ColumnInfo(name = "following_id") val followingID: String
) {
    @PrimaryKey val pid: Long = 0
}