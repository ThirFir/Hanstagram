package com.dbclass.hanstagram.data.repository.dislike

import com.dbclass.hanstagram.data.db.dislikes.DislikeEntity
import com.dbclass.hanstagram.data.db.dislikes.DislikesDao
import com.dbclass.hanstagram.data.repository.Repository

abstract class DislikeRepository: Repository {
    protected var dao: DislikesDao? = null

    abstract suspend fun doDislike(dislike: DislikeEntity): DislikeEntity?
    abstract suspend fun cancelDislike(pid: Long)
    abstract suspend fun getDislikesCount(postID: Long): Long
    abstract suspend fun getDislike(userID: String, postID: Long): DislikeEntity?
}
