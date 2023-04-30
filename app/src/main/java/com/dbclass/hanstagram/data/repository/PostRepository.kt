package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.posts.PostsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object PostRepository {
    private var postsDao: PostsDao? = null

    fun init(appContext: Context) {
        postsDao = HanstagramDatabase.getInstance(appContext)?.postsDao()
    }

    fun createPost(post: PostEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            postsDao?.insertPost(post)
        }
    }

    fun updatePost(post: PostEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            postsDao?.updatePost(post)
        }
    }

    fun deletePost(postID: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            postsDao?.deletePost(postID)
        }
    }

    suspend fun getPostsCount(userID: String): Int {
        return CoroutineScope(Dispatchers.Default).async {
            postsDao?.getPostsCount(userID)
        }.await() ?: 0
    }
}