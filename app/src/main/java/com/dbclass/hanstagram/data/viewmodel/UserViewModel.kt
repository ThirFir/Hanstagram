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

    fun setProfileImage(uri: String) {
        val updatedUser = user.value?.apply {
            profileImage = uri
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null)
            UserRepository.updateProfileImage(user.value?.id!!, uri)
    }

    fun setNickname(nickname: String) {
        val updatedUser = user.value?.apply {
            this.nickname = nickname
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null)
            UserRepository.updateNickname(user.value?.id!!, nickname)
    }

    fun setTemperature(t: Float) {
        val updatedUser = user.value?.apply {
            temperature = t
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null)
            UserRepository.updateTemperature(user.value?.id!!, t)

    }

    fun setCaption(caption: String) {
        val updatedUser = user.value?.apply {
            this.caption = caption
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null)
            UserRepository.updateCaption(user.value?.id!!, caption)
    }

}