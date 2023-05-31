package com.dbclass.hanstagram.data.db.posts

import androidx.room.*
import com.dbclass.hanstagram.data.db.users.UserEntity

@Dao
interface PostsDao {

    @Insert
    suspend fun insertPost(post: PostEntity)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("update posts set images=:images, content=:content where post_id=:postID")
    suspend fun updatePost(postID: Long, images: String, content: String)

    @Query("delete from posts where post_id = :postID")
    suspend fun deletePost(postID: Long)

    @Query("select * from posts inner join users on posts.user_id = users.id where post_id=:postID")
    suspend fun getPostWithUser(postID: Long): Map<PostEntity, UserEntity>

    @Query("select * from posts inner join users on posts.user_id = users.id order by posts.created_time desc")
    suspend fun getAllPostsWithUser(): Map<PostEntity, UserEntity>

    @Query("select * from posts where user_id=:userID")
    suspend fun getUserPosts(userID: String): List<PostEntity>
    @Query(
        "select * from posts inner join users on posts.user_id = users.id where user_id in " +
                "(select following_id from follows where follower_id=:id) order by posts.created_time desc")
    suspend fun getFollowingPostsWithUser(id: String): Map<PostEntity, UserEntity>

    @Query("select count(post_id) from posts where user_id = :userID")
    suspend fun getPostsCount(userID: String): Long

    @Query("SELECT count(pid) from likes where post_id = :postID")
    suspend fun getPostsLikesCount(postID: Long): Long

    @Query("SELECT count(pid) from dislikes where post_id = :postID")
    suspend fun getPostsDislikesCount(postID: Long): Long

    @Query("SELECT count(pid) from comments where post_id = :postID")
    suspend fun getPostsCommentsCount(postID: Long): Long

}