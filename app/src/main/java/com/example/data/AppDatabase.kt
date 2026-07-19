package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReadingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taro_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class ReadingRepository(private val readingDao: ReadingDao) {
    val allReadings = readingDao.getAllReadings()

    suspend fun insert(reading: ReadingEntity) {
        readingDao.insertReading(reading)
    }

    suspend fun delete(id: Int) {
        readingDao.deleteReadingById(id)
    }
}
