package com.dbclass.hanstagram.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.UserRepository

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    fun createUser(user: UserEntity) {
        setUser(user)
        UserRepository.createUser(user)
    }

    fun setUser(user: UserEntity) {
        _user.value = user
    }

    fun setProfileImage(url: String) {
        _user.value?.profileImage = url
        if (user.value?.id != null)
            UserRepository.updateProfileImage(user.value?.id!!, url)
    }

    fun setNickname(nickname: String) {
        _user.value?.nickname = nickname
        if (user.value?.id != null)
            UserRepository.updateNickname(user.value?.id!!, nickname)
    }

    fun setTemperature(t: Float) {
        _user.value?.temperature = t
        if (user.value?.id != null)
            UserRepository.updateTemperature(user.value?.id!!, t)

    }

    fun setCaption(caption: String) {
        _user.value?.caption = caption
        if (user.value?.id != null)
            UserRepository.updateCaption(user.value?.id!!, caption)
    }

}