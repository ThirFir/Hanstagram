package com.dbclass.hanstagram.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UserEntity>()
    private val userRepository: UserRepository = UserRepositoryImpl
    val user: LiveData<UserEntity>
        get() = _user

    fun createUser(user: UserEntity) {
        setUser(user)
        CoroutineScope(Dispatchers.Main).launch {
            userRepository.createUser(user)
        }
    }

    fun setUser(user: UserEntity) {
        _user.value = user
    }

    fun setProfileImage(uri: String) {
        val updatedUser = user.value?.apply {
            profileImage = uri
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null) {
            CoroutineScope(Dispatchers.Main).launch {
                userRepository.updateProfileImage(user.value?.id!!, uri)
            }
        }
    }

    fun setNickname(nickname: String) {
        val updatedUser = user.value?.apply {
            this.nickname = nickname
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null) {
            CoroutineScope(Dispatchers.Main).launch {
                userRepository.updateNickname(user.value?.id!!, nickname)
            }
        }
    }

    fun setTemperature(delta: Float) {
        val updatedUser = user.value?.apply {
            temperature += delta
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null) {
            CoroutineScope(Dispatchers.Main).launch {
                userRepository.updateTemperature(user.value?.id!!, user.value?.temperature!!)
            }
        }
    }

    fun setCaption(caption: String) {
        val updatedUser = user.value?.apply {
            this.caption = caption
        } ?: return
        _user.value = updatedUser
        if (user.value?.id != null) {
            CoroutineScope(Dispatchers.Main).launch {
                userRepository.updateCaption(user.value?.id!!, caption)
            }
        }
    }

}