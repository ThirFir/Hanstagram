package com.dbclass.hanstagram.data.db.dislikes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface DislikesDao {

    @Insert
    fun insertDislike(dislike: DislikeEntity)

    @Delete
    fun deleteDislike(dislike: DislikeEntity)
}