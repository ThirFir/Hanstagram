package com.dbclass.hanstagram.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbclass.hanstagram.db.comments.CommentEntity
import com.dbclass.hanstagram.db.comments.CommentsDao
import com.dbclass.hanstagram.db.follows.FollowEntity
import com.dbclass.hanstagram.db.follows.FollowsDao
import com.dbclass.hanstagram.db.likes.LikeEntity
import com.dbclass.hanstagram.db.likes.LikesDao
import com.dbclass.hanstagram.db.posts.PostEntity
import com.dbclass.hanstagram.db.posts.PostsDao
import com.dbclass.hanstagram.db.users.UserEntity
import com.dbclass.hanstagram.db.users.UsersDao

@Database(entities = [UserEntity::class, PostEntity::class, LikeEntity::class, FollowEntity::class, CommentEntity::class], version = 2)
abstract class HanstagramDatabase : RoomDatabase() {
    abstract fun usersDao() : UsersDao
    abstract fun postsDao() : PostsDao
    abstract fun likesDao() : LikesDao
    abstract fun followsDao() : FollowsDao
    abstract fun commentsDao() : CommentsDao
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