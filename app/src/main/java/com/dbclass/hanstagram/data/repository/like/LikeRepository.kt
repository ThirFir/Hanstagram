package com.dbclass.hanstagram.data.repository.like

import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.data.db.likes.LikesDao
import com.dbclass.hanstagram.data.repository.Repository

abstract class LikeRepository : Repository {
    var dao: LikesDao? = null

    abstract suspend fun doLike(like: LikeEntity): LikeEntity?
    abstract suspend fun cancelLike(pid: Long)
    abstract suspend fun getLike(userID: String, postID: Long): LikeEntity?
    abstract suspend fun getLikesCount(postID: Long): Long
}