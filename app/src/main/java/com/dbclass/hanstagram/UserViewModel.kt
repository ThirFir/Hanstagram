package com.dbclass.hanstagram

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbclass.hanstagram.db.users.UserEntity

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    fun setUser(user: UserEntity) {
        _user.value = user
    }
}