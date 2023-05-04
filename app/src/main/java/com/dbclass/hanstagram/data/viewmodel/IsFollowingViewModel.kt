package com.dbclass.hanstagram.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbclass.hanstagram.data.repository.FollowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IsFollowingViewModel: ViewModel() {
    private val _followPID = MutableLiveData<Long?>()        // == null 이면 팔로우 X
    val followPID: LiveData<Long?>
        get() = _followPID

    private val _followerCount = MutableLiveData<Long>()
    val followerCount: LiveData<Long>
        get() = _followerCount

    var observeCuzInitialize = false

    fun follow(follower: String, following: String) {
        if(followPID.value != null) {
            FollowRepository.doUnFollow(followPID.value!!)
            _followPID.value = null
            _followerCount.value = _followerCount.value!! - 1
        }
        else {
            CoroutineScope(Dispatchers.Default).launch {
                val newPID = FollowRepository.doFollow(follower = follower, following = following)
                Log.d("doFollow", newPID.toString())
                _followPID.postValue(newPID)
                _followerCount.postValue(followerCount.value!! + 1)
            }
        }
    }

    fun initializeFollow(follower: String?, following: String?) {
        CoroutineScope(Dispatchers.Default).launch {
            if(follower != null && following != null) {
                val followPID = FollowRepository.getFollowPID(follower = follower, following = following)
                _followPID.postValue(followPID)
                val count = FollowRepository.getFollowersCount(following)
                _followerCount.postValue(count)
            }
            observeCuzInitialize = true
        }
    }
}