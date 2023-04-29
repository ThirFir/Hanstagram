package com.dbclass.hanstagram.data.repository

import android.content.Context
import android.net.Uri
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.db.users.UsersDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UserRepository {
    private var usersDao: UsersDao? = null

    fun init(appContext: Context) {
        usersDao = HanstagramDatabase.getInstance(appContext)?.usersDao()
    }

    fun createUser(user: UserEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.insertUser(user)
        }
    }

    fun updateProfileImage(id: String, uri: Uri) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.updateProfileImage(id, uri.toString())
        }
    }
    fun updateProfileImage(id: String, uri: String) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.updateProfileImage(id, uri)
        }
    }

    fun updateNickname(id: String, nickname: String) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.updateNickname(id, nickname)
        }
    }

    fun updateTemperature(id: String, t: Float) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.updateTemperature(id, t)
        }
    }

    fun updateCaption(id: String, caption: String) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.updateCaption(id, caption)
        }
    }

    fun updateDepartment(id: String, department: String) {
        CoroutineScope(Dispatchers.Default).launch {
            usersDao?.updateDepartment(id, department)
        }
    }
}