package com.dbclass.hanstagram.data.repository.guest

import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.db.guests.GuestCommentsDao
import com.dbclass.hanstagram.data.repository.Repository

abstract class GuestCommentRepository: Repository {
    var dao: GuestCommentsDao? = null

    abstract suspend fun addComment(guestComment: GuestCommentEntity)
    abstract suspend fun deleteComment(guestComment: GuestCommentEntity)
    abstract suspend fun getGuestComments(ownerUserID: String) : List<GuestCommentEntity>
}