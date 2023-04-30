package com.dbclass.hanstagram.data.db.posts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostsDao {

    @Insert
    fun insertPost(post: PostEntity)

    @Update
    fun updatePost(post: PostEntity)

    @Query("delete from posts where post_id = :postID")
    fun deletePost(postID: Long)

    @Query("select * from posts")
    fun getAllPosts(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE user_id IN (SELECT following_id FROM follows WHERE follower_id=:id)")
    fun getFollowingPosts(id: String): List<PostEntity>

    @Query("select post_id from posts where user_id = :userID")
    fun getPostsCount(userID: String): Int

    @Query("select post_id from posts where post_id = :postID")
    fun getPostsLikesCount(postID: Long): Long

    @Query("select post_id from posts where post_id = :postID")
    fun getPostsDislikesCount(postID: Long): Long
}