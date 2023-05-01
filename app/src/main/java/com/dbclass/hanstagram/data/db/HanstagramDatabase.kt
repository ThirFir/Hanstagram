package com.dbclass.hanstagram.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbclass.hanstagram.data.db.comments.CommentEntity
import com.dbclass.hanstagram.data.db.comments.CommentsDao
import com.dbclass.hanstagram.data.db.dislikes.DislikeEntity
import com.dbclass.hanstagram.data.db.dislikes.DislikesDao
import com.dbclass.hanstagram.data.db.follows.FollowEntity
import com.dbclass.hanstagram.data.db.follows.FollowsDao
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.db.guests.GuestCommentsDao
import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.data.db.likes.LikesDao
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.db.posts.PostsDao
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.db.users.UsersDao

@Database(
    entities = [UserEntity::class, PostEntity::class, LikeEntity::class, FollowEntity::class,
        CommentEntity::class, DislikeEntity::class, GuestCommentEntity::class],
    version = 1
)
abstract class HanstagramDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun postsDao(): PostsDao
    abstract fun likesDao(): LikesDao
    abstract fun dislikesDao(): DislikesDao
    abstract fun followsDao(): FollowsDao
    abstract fun commentsDao(): CommentsDao

    abstract fun guestCommentsDao(): GuestCommentsDao

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