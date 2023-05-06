package com.dbclass.hanstagram.data.db.posts

import androidx.room.*

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

    @Query("select * from posts where user_id=:userID")
    fun getUserPosts(userID: String): List<PostEntity>

    @Query("SELECT * FROM posts WHERE user_id IN " +
            "(SELECT following_id FROM follows WHERE follower_id=:id) " +
            "order by posts.created_time desc")
    fun getFollowingPosts(id: String): List<PostEntity>

    @Query("select count(post_id) from posts where user_id = :userID")
    fun getPostsCount(userID: String): Long

    @Query("SELECT count(pid) from likes where post_id = :postID")
    fun getPostsLikesCount(postID: Long): Long

    @Query("SELECT count(pid) from dislikes where post_id = :postID")
    fun getPostsDislikesCount(postID: Long): Long

    @Query("SELECT count(pid) from comments where post_id = :postID")
    fun getPostsCommentsCount(postID: Long): Long
}