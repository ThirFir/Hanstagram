package com.dbclass.hanstagram.db.users

import android.database.Observable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["nickname", "email", "studentID"], unique = true)])
data class UserEntity(
    @PrimaryKey val id: String,
    var password: String,
    var nickname: String,
    val email: String,
    val studentID: String,
    var temperature: Float = 36.5f,
    val timestamp: Long,

    val profileImage: String? = null,
    val captionName: String = "",
    val caption: String = ""

)
