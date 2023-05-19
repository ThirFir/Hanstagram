package com.dbclass.hanstagram.data.repository.guest

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.db.guests.GuestCommentsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object GuestCommentRepositoryImpl: GuestCommentRepository() {

    override fun init(appContext: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            dao = HanstagramDatabase.getInstance(appContext)?.guestCommentsDao()
        }
    }

    override suspend fun addComment(guestComment: GuestCommentEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.insertGuest(guestComment)
        }
    }

    override suspend fun deleteComment(guestComment: GuestCommentEntity) {
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.deleteGuest(guestComment)
        }
    }

    override suspend fun getGuestComments(ownerUserID: String) : List<GuestCommentEntity> =
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            dao?.getGuestComments(ownerUserID) ?: listOf()
        }
}