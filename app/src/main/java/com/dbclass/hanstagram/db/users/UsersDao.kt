package com.dbclass.hanstagram.db.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun createUser(user: UserEntity)

    @Query("select * from user")
    fun getAll(): List<UserEntity>

    @Query("select * from user where id = :id")
    fun getUser(id: String): UserEntity

    @Query("delete from user where id = :id")
    fun deleteUser(id: String)

    @Query("update user set password = :password where id = :id")
    fun updatePassword(password: String, id: String)

    @Query("update user set nickname = :nickname where id = :id")
    fun updateNickname(nickname: String, id: String)
}