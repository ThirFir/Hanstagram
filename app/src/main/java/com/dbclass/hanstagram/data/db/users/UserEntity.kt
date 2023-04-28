package com.dbclass.hanstagram.data.db.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["id", "email", "studentID"], unique = true)])
data class UserEntity(
    @PrimaryKey val id: String,
    var password: String,
    var nickname: String,
    var email: String,
    @ColumnInfo(name = "student_id") val studentID: String,
    var temperature: Float = 36.5f,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    var department: String = "",    // TODO - department category
    @ColumnInfo(name = "profile_img") var profileImage: String? = null,
    var caption: String? = null

)
