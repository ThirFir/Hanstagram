package com.dbclass.hanstagram.data.repository.user

import android.content.Context
import android.net.Uri
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.db.users.UsersDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object UserRepositoryImpl : UserRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.usersDao()
        }
    }

    override suspend fun createUser(user: UserEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.insertUser(user)
        }
    }

    override suspend fun updateProfileImage(id: String, uri: Uri) {
        withContext(dbScope.coroutineContext) {
            dao?.updateProfileImage(id, uri.toString())
        }
    }
    override suspend fun updateProfileImage(id: String, uri: String) {
        withContext(dbScope.coroutineContext) {
            dao?.updateProfileImage(id, uri)
        }
    }

    override suspend fun updateNickname(id: String, nickname: String) {
        withContext(dbScope.coroutineContext) {
            dao?.updateNickname(id, nickname)
        }
    }

    override suspend fun updateTemperature(id: String, updatedTemperature: Float) {
        withContext(dbScope.coroutineContext) {
            dao?.updateTemperature(id, updatedTemperature)
        }
    }

    override suspend fun updateCaption(id: String, caption: String) {
        withContext(dbScope.coroutineContext) {
            dao?.updateCaption(id, caption)
        }
    }

    override suspend fun updateDepartment(id: String, department: String) {
        withContext(dbScope.coroutineContext) {
            dao?.updateDepartment(id, department)
        }
    }

    override suspend fun getUser(id: String): UserEntity? {
        return withContext(dbScope.coroutineContext) {
            dao?.getUser(id)
        }
    }

    override suspend fun findUsers(idInput: String): List<UserEntity> {
        return withContext(dbScope.coroutineContext) {
            dao?.getContainingInputUsers("%$idInput%") ?: listOf()
        }
    }

    override suspend fun getNickname(userID: String): String? {
        return withContext(dbScope.coroutineContext) {
            dao?.getNickname(userID)
        }
    }
    override suspend fun getProfileImage(userID: String): String? {
        return withContext(dbScope.coroutineContext) {
            dao?.getProfileImage(userID)
        }
    }

    override suspend fun deleteUser(userID: String) {
        withContext(dbScope.coroutineContext) {
            dao?.deleteUser(userID)
        }
    }

    override suspend fun getTemperature(userID: String): Float {
        return withContext(dbScope.coroutineContext) {
            dao?.getTemperature(userID) ?: 0f
        }
    }

}