package com.dbclass.hanstagram.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbclass.hanstagram.data.db.posts.PostEntity

class PostViewModel : ViewModel() {
    private val _post = MutableLiveData<PostEntity>()
    val post : LiveData<PostEntity>
        get() = _post
}