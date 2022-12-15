package com.example.nancost.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nancost.model.Nancost

@Database(entities = [Nancost::class], version = 1, exportSchema = false)
abstract class NancostDatabase : RoomDatabase() {
    abstract fun nancostDao(): NancostDao

    companion object {
        const val VERSION_DATABASE = 4
        private const val DATABASE_NAME = "nancost"

        fun getInstance(context: Context): NancostDatabase =
            Room.databaseBuilder(
                context,
                NancostDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}