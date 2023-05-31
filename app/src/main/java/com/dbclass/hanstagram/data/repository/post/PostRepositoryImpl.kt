package com.dbclass.hanstagram.data.repository.post

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.posts.PostsDao
import com.dbclass.hanstagram.data.db.users.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object PostRepositoryImpl : PostRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.postsDao()
        }
    }

    override suspend fun addPost(post: PostEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.insertPost(post)
        }
    }

    override suspend fun updatePost(post: PostEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.updatePost(post)
        }
    }

    override suspend fun updatePost(postID: Long, images: String, content: String) {
        withContext(dbScope.coroutineContext) {
            dao?.updatePost(postID, images, content)
        }
    }

    override suspend fun deletePost(postID: Long) {
        withContext(dbScope.coroutineContext) {
            dao?.deletePost(postID)
        }
    }

    override suspend fun getPostsCount(userID: String): Long {
        return withContext(dbScope.coroutineContext) {
            dao?.getPostsCount(userID)
        } ?: 0
    }

    override suspend fun getUserPosts(userID: String?): List<PostEntity> {
        return if(userID != null) {
            withContext(dbScope.coroutineContext) {
                dao?.getUserPosts(userID) ?: listOf()
            }
        } else listOf()
    }

    override suspend fun getFollowingPostsWithUsers(userID: String?): Map<PostEntity, UserEntity> {
        return if(userID != null) {
            withContext(dbScope.coroutineContext) {
                dao?.getFollowingPostsWithUser(userID) ?: mapOf()
            }
        } else mapOf()
    }


    override suspend fun getAllPostsWithUsers(limit: Int): Map<PostEntity, UserEntity> {
        return withContext(dbScope.coroutineContext) {
            dao?.getAllPostsWithUser() ?: mapOf()
        }
    }

    override suspend fun getPostWithUser(postID: Long?): Map<PostEntity, UserEntity>? {
        return if (postID != null) {
            withContext(dbScope.coroutineContext) {
                dao?.getPostWithUser(postID)
            }
        } else null
    }
}