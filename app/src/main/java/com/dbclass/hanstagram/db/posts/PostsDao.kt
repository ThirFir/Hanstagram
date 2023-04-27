package com.dbclass.hanstagram.db.posts

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PostsDao {

    @Query("select * from posts")
    fun getAllPosts(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE userID IN (SELECT followingID FROM follows WHERE followerID=:followerID)")
    fun getFollowingPosts(followerID: String): List<PostEntity>
}