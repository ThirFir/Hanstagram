package com.dbclass.hanstagram.data.db.guests

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.users.UserEntity

@Entity(tableName = "guest_book", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["guest_user_id"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["owner_user_id"],
        onDelete = CASCADE
    )
])
data class GuestCommentEntity (
    @ColumnInfo(name = "guest_user_id") val guestUserID: String,
    @ColumnInfo(name = "owner_user_id") val ownerUserID: String,
    val comment: String,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    @PrimaryKey(autoGenerate = true) val pid: Long = 0,
)