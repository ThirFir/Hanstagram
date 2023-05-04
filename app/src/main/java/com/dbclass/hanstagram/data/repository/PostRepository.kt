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

    fun addPost(post: PostEntity) {
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

    suspend fun getPostsCount(userID: String): Long {
        return CoroutineScope(Dispatchers.Default).async {
            postsDao?.getPostsCount(userID)
        }.await() ?: 0
    }

    suspend fun getFollowingPosts(userID: String): List<PostEntity> {
        return CoroutineScope(Dispatchers.Default).async {
            postsDao?.getFollowingPosts(userID) ?: listOf()
        }.await()
    }

    suspend fun getAllPosts(limit: Int): List<PostEntity> {
        return CoroutineScope(Dispatchers.Default).async {
            postsDao?.getAllPosts() ?: listOf()
        }.await()
    }
}