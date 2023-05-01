package com.dbclass.hanstagram.data.repository

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.db.guests.GuestCommentsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object GuestCommentRepository {
    private var guestCommentsDao: GuestCommentsDao? = null

    fun init(appContext: Context) {
        guestCommentsDao = HanstagramDatabase.getInstance(appContext)?.guestCommentsDao()
    }

    fun addComment(guestComment: GuestCommentEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            guestCommentsDao?.insertGuest(guestComment)
        }
    }

    fun deleteComment(guestComment: GuestCommentEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            guestCommentsDao?.deleteGuest(guestComment)
        }
    }

    suspend fun getGuestComments(ownerUserID: String) : List<GuestCommentEntity> = CoroutineScope(Dispatchers.Default).async {
        guestCommentsDao?.getGuestComments(ownerUserID)
    }.await() ?: listOf()
}