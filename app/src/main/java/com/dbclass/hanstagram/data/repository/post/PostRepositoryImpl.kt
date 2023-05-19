package com.dbclass.hanstagram.data.repository.post

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.posts.PostsDao
import com.dbclass.hanstagram.data.db.users.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object PostRepositoryImpl : PostRepository() {

    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.postsDao()
        }
    }

    override suspend fun addPost(post: PostEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertPost(post)
        }
    }

    override suspend fun updatePost(post: PostEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.updatePost(post)
        }
    }

    override suspend fun deletePost(postID: Long) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.deletePost(postID)
        }
    }

    override suspend fun getPostsCount(userID: String): Long {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getPostsCount(userID)
        } ?: 0
    }

    override suspend fun getUserPosts(userID: String?): List<PostEntity> {
        return if(userID != null) {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                dao?.getUserPosts(userID) ?: listOf()
            }
        } else listOf()
    }

    override suspend fun getFollowingPostsWithUsers(userID: String?): Map<PostEntity, UserEntity> {
        return if(userID != null) {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                dao?.getFollowingPostsWithUser(userID) ?: mapOf()
            }
        } else mapOf()
    }


    override suspend fun getAllPostsWithUsers(limit: Int): Map<PostEntity, UserEntity> {
        return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getAllPostsWithUser() ?: mapOf()
        }
    }

    override suspend fun getPostWithUser(postID: Long?): Map<PostEntity, UserEntity>? {
        return if (postID != null) {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                dao?.getPostWithUser(postID)
            }
        } else null
    }
}