package com.dbclass.hanstagram.data.db.likes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface LikesDao {

    @Insert
    fun insertLike(like: LikeEntity)

    @Delete
    fun deleteLike(like: LikeEntity)
}