package com.dbclass.hanstagram.db.account

import androidx.room.*

@Dao
interface AccountDao {

    @Query("select * from account")
    fun getAll(): List<AccountEntity>

    @Query("select * from account where id = :id")
    fun getAccount(id: String): AccountEntity

    @Query("delete from account where id = :id")
    fun deleteAccount(id: String)

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun createAccount(account: AccountEntity)
}
