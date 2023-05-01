package com.dbclass.hanstagram.data.db.guests

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guest_book")
data class GuestCommentEntity (
    @ColumnInfo(name = "guest_user_id") val guestUserID: String,
    @ColumnInfo(name = "owner_user_id") val ownerUserID: String,
    val comment: String,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    @PrimaryKey(autoGenerate = true) val pid: Long = 0,
)