package com.dbclass.hanstagram.db.account

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AccountEntity::class], version = 1)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao() : AccountDao

    companion object {
        private var instance: AccountDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AccountDatabase? {
            if (instance == null)
                synchronized(AccountDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AccountDatabase::class.java,
                        "account.db"
                    ).build()
                }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}