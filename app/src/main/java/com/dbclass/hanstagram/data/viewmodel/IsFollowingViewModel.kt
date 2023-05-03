package com.dbclass.hanstagram.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbclass.hanstagram.data.repository.FollowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IsFollowingViewModel: ViewModel() {
    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean>
        get() = _isFollowing

    var observe_cuz_initialize = false


    fun follow(followFrom: String, followTo: String) {
        if(isFollowing.value == true)
            FollowRepository.doUnFollow(follower = followFrom, following = followTo)
        else
            FollowRepository.doFollow(follower = followFrom, following = followTo)

        _isFollowing.value = !(isFollowing.value ?: true)
    }

    fun initializeFollowingState(followerID: String?, followingID: String?) {
        CoroutineScope(Dispatchers.Default).launch {
            if(followerID != null && followingID != null)
                _isFollowing.postValue(
                    FollowRepository.isFollowing(following = followingID, follower = followerID))
            observe_cuz_initialize = true
        }
    }
}