package com.dbclass.hanstagram.db.users

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey



@Entity(tableName = "users", indices = [Index(value = ["nickname", "email"], unique = true)])
data class UserEntity(
    @PrimaryKey val id: String,
    var password: String,
    var nickname: String,
    val email: String,


    // val createdAt: Long, TODO : TIMESTAMP

)
