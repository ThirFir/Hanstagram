package com.dbclass.hanstagram.data.repository.post

import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.posts.PostsDao
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.Repository

abstract class PostRepository : Repository {
    var dao: PostsDao? = null

    abstract suspend fun addPost(post: PostEntity)
    abstract suspend fun updatePost(post: PostEntity)
    abstract suspend fun deletePost(postID: Long)
    abstract suspend fun getPostsCount(userID: String): Long
    abstract suspend fun getUserPosts(userID: String?): List<PostEntity>
    abstract suspend fun getFollowingPostsWithUsers(userID: String?): Map<PostEntity, UserEntity>
    abstract suspend fun getAllPostsWithUsers(limit: Int): Map<PostEntity, UserEntity>
    abstract suspend fun getPostWithUser(postID: Long?): Map<PostEntity, UserEntity>?
}