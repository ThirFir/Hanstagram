package com.dbclass.hanstagram.db.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun createUser(user: UserEntity)

    @Query("select * from users")
    fun getAll(): List<UserEntity>

    @Query("select * from users where id = :id")
    fun getUser(id: String): UserEntity

    @Query("delete from users where id = :id")
    fun deleteUser(id: String)

    @Query("update users set password = :password where id = :id")
    fun updatePassword(password: String, id: String)

    @Query("update users set nickname = :nickname where id = :id")
    fun updateNickname(nickname: String, id: String)
}