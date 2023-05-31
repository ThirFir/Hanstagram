package com.dbclass.hanstagram.data.repository.guest

import android.content.Context
import com.dbclass.hanstagram.data.db.HanstagramDatabase
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.db.guests.GuestCommentsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object GuestCommentRepositoryImpl: GuestCommentRepository() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val dbScope: CoroutineScope = CoroutineScope(defaultDispatcher)

    override fun init(appContext: Context) {
        dbScope.launch {
            dao = HanstagramDatabase.getInstance(appContext)?.guestCommentsDao()
        }
    }

    override suspend fun addComment(guestComment: GuestCommentEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.insertGuest(guestComment)
        }
    }

    override suspend fun deleteComment(guestComment: GuestCommentEntity) {
        withContext(dbScope.coroutineContext) {
            dao?.deleteGuest(guestComment)
        }
    }

    override suspend fun getGuestComments(ownerUserID: String) : List<GuestCommentEntity> =
        withContext(dbScope.coroutineContext) {
            dao?.getGuestComments(ownerUserID) ?: listOf()
        }
}