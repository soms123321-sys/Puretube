package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        HistoryEntity::class,
        BookmarkEntity::class,
        SubscriptionEntity::class,
        PlaylistEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PureTubeDatabase : RoomDatabase() {
    abstract fun pureTubeDao(): PureTubeDao

    companion object {
        @Volatile
        private var INSTANCE: PureTubeDatabase? = null

        fun getDatabase(context: Context): PureTubeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PureTubeDatabase::class.java,
                    "puretube_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
