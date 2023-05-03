package com.dbclass.hanstagram.data.db.messages

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.dbclass.hanstagram.data.db.users.UserEntity


@Entity(tableName = "messages", foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["from_user_id"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["to_user_id"],
        onDelete = CASCADE
    )
])
data class MessageEntity(
    @ColumnInfo(name = "from_user_id") val fromUserID: String,
    @ColumnInfo(name = "to_user_id") val toUserID: String,
    @ColumnInfo(name = "is_read") var isRead: Boolean,
    var content: String?,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    @PrimaryKey(autoGenerate = true) val pid: Long = 0
)