package com.ahmad.kotlinroomdatabase.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmad.kotlinroomdatabase.Local.UserDatabase.Companion.DATABASE_VERSION
import com.ahmad.kotlinroomdatabase.Model.User

@Database(entities = arrayOf(User::class),version = DATABASE_VERSION)
abstract class UserDatabase:RoomDatabase(){
    abstract fun userDAO():UserDAO

    companion object{
        const val DATABASE_VERSION = 1
        val DATABASE_NAME = "EDMT-Database-Room"

        private var mInstance:UserDatabase?=null

        fun getInstance(context: Context):UserDatabase{
            if (mInstance == null)
                mInstance = Room.databaseBuilder(context,UserDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

            return mInstance!!
        }
    }
}