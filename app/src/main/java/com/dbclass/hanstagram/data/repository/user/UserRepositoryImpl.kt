package com.dbclass.hanstagram.data.repository.user

import android.content.Context
import android.net.Uri
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.db.users.UsersDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object UserRepositoryImpl : UserRepository() {

    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.usersDao()
        }
    }

    override suspend fun createUser(user: UserEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertUser(user)
        }
    }

    override suspend fun updateProfileImage(id: String, uri: Uri) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateProfileImage(id, uri.toString())
        }
    }
    override suspend fun updateProfileImage(id: String, uri: String) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateProfileImage(id, uri)
        }
    }

    override suspend fun updateNickname(id: String, nickname: String) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateNickname(id, nickname)
        }
    }

    override suspend fun updateTemperature(id: String, updatedTemperature: Float) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateTemperature(id, updatedTemperature)
        }
    }

    override suspend fun updateCaption(id: String, caption: String) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateCaption(id, caption)
        }
    }

    override suspend fun updateDepartment(id: String, department: String) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updateDepartment(id, department)
        }
    }

    override suspend fun getUser(id: String): UserEntity? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getUser(id)
        }
    }

    override suspend fun findUsers(idInput: String): List<UserEntity> {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getContainingInputUsers("%$idInput%") ?: listOf()
        }
    }

    override suspend fun getNickname(userID: String): String? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getNickname(userID)
        }
    }
    override suspend fun getProfileImage(userID: String): String? {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getProfileImage(userID)
        }
    }

    override suspend fun deleteUser(userID: String) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.deleteUser(userID)
        }
    }

}