package com.dbclass.hanstagram.data.db.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun insertUser(user: UserEntity)

    @Query("select * from users")
    fun getAll(): List<UserEntity>

    @Query("select * from users where id = :id")
    fun getUser(id: String): UserEntity

    @Query("select * from users where id=:input")   // TODO
    fun getContainingInputUsers(input: String): List<UserEntity>
    @Query("delete from users where id = :id")
    fun deleteUser(id: String)

    @Query("update users set password = :password where id = :id")
    fun updatePassword(id: String, password: String)

    @Query("update users set nickname = :nickname where id = :id")
    fun updateNickname(id: String, nickname: String)

    @Query("update users set temperature = :t where id = :id")
    fun updateTemperature(id: String, t: Float)

    @Query("update users set profile_image = :uri where id = :id")
    fun updateProfileImage(id: String, uri: String)

    @Query("update users set caption = :caption where id = :id")
    fun updateCaption(id: String, caption: String)

    @Query("update users set department = :department where id = :id")
    fun updateDepartment(id: String, department: String)
}