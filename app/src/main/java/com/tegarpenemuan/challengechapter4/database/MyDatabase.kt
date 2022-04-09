package com.tegarpenemuan.challengechapter4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tegarpenemuan.challengechapter4.data.local.dao.NoteDAO
import com.tegarpenemuan.challengechapter4.data.local.dao.UserDAO
import com.tegarpenemuan.challengechapter4.data.local.entity.NoteEntity
import com.tegarpenemuan.challengechapter4.data.local.entity.UserEntity

@Database(entities = [NoteEntity::class, UserEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun noteDAO(): NoteDAO
    abstract fun userDAO(): UserDAO

    companion object {
        private const val DB_NAME = "My.db"

        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): MyDatabase {
            return Room.databaseBuilder(context, MyDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}