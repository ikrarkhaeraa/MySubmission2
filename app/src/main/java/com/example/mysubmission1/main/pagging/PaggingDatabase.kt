package com.example.mysubmission1.main.pagging

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mysubmission1.main.response.GetAllStoryResponse
import com.example.mysubmission1.main.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class PaggingDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: PaggingDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PaggingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PaggingDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}