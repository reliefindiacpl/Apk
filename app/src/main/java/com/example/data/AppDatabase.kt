package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Enquiry::class, Payment::class, CmsConfig::class, CmsService::class, CmsSlide::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun enquiryDao(): EnquiryDao
    abstract fun paymentDao(): PaymentDao
    abstract fun cmsDao(): CmsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "relief_india_construction_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
