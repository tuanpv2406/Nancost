package com.example.nancost.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class NancostDatabase : RoomDatabase() {
    abstract fun nancostDao(): NancostDao

    companion object{
        @Volatile
        private var INSTANCE: NancostDatabase? = null

        fun getDatabase(context: Context): NancostDatabase {
            val tempInstance = INSTANCE

//            check if there is any existing instance is present for our room database
//            if there exist an existing instance then we'll return that instance
            if (tempInstance != null) {
                return tempInstance
            }

//            If there is no any instance present for our database then we'll create a new instance
//            WHY SYNCHRONIZED ?? --> Because everything inside the synchronized block will be protected
//            by concurrent execution on multiple threads
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NancostDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}