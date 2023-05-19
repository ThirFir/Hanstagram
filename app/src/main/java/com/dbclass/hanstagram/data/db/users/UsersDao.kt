package com.dbclass.hanstagram.data.db.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UsersDao {


    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertUser(user: UserEntity)

    @Query("select * from users")
    suspend fun getAll(): List<UserEntity>

    @Query("select * from users where id = :id")
    suspend fun getUser(id: String): UserEntity

    @Query("select nickname from users where id = :id")
    suspend fun getNickname(id: String): String

    @Query("select profile_image from users where id = :id")
    suspend fun getProfileImage(id: String): String

    @Query("SELECT * from users where id like :input")
    suspend fun getContainingInputUsers(input: String): List<UserEntity>
    @Query("delete from users where id = :id")
    suspend fun deleteUser(id: String)

    @Query("update users set password = :password where id = :id")
    suspend fun updatePassword(id: String, password: String)

    @Query("update users set nickname = :nickname where id = :id")
    suspend fun updateNickname(id: String, nickname: String)

    @Query("update users set temperature = :t where id = :id")
    suspend fun updateTemperature(id: String, t: Float)

    @Query("update users set profile_image = :uri where id = :id")
    suspend fun updateProfileImage(id: String, uri: String)

    @Query("update users set caption = :caption where id = :id")
    suspend fun updateCaption(id: String, caption: String)

    @Query("update users set department = :department where id = :id")
    suspend fun updateDepartment(id: String, department: String)

    @Query("select temperature from users where id=:id")
    suspend fun getTemperature(id: String): Float
}