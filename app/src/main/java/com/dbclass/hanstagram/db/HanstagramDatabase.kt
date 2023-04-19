package com.dbclass.hanstagram.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbclass.hanstagram.db.users.UserEntity
import com.dbclass.hanstagram.db.users.UsersDao

@Database(entities = [UserEntity::class], version = 1)
abstract class HanstagramDatabase : RoomDatabase() {
    abstract fun usersDao() : UsersDao

    companion object {
        private var instance: HanstagramDatabase? = null

        @Synchronized
        fun getInstance(context: Context): HanstagramDatabase? {
            if (instance == null)
                synchronized(HanstagramDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HanstagramDatabase::class.java,
                        "hanstagram.db"
                    ).build()
                }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}