package com.dbclass.hanstagram.db.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val id: String = "",
    val password: String = ""
)