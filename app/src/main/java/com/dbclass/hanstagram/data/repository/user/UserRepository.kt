package com.dbclass.hanstagram.data.repository.user

import android.net.Uri
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.db.users.UsersDao
import com.dbclass.hanstagram.data.repository.Repository

abstract class UserRepository : Repository {
    protected var dao: UsersDao? = null

    abstract suspend fun createUser(user: UserEntity)
    abstract suspend fun updateProfileImage(id: String, uri: Uri)
    abstract suspend fun updateProfileImage(id: String, uri: String)
    abstract suspend fun updateNickname(id: String, nickname: String)
    abstract suspend fun updateTemperature(id: String, updatedTemperature: Float)
    abstract suspend fun updateCaption(id: String, caption: String)
    abstract suspend fun updateDepartment(id: String, department: String)
    abstract suspend fun getUser(id: String): UserEntity?
    abstract suspend fun findUsers(idInput: String): List<UserEntity>
    abstract suspend fun getNickname(userID: String): String?
    abstract suspend fun getProfileImage(userID: String): String?
    abstract suspend fun deleteUser(userID: String)
    abstract suspend fun getTemperature(userID: String): Float
}