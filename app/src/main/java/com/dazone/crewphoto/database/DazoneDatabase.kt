package com.dazone.crewphoto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dazone.crewphoto.model.User
import com.dazone.crewphoto.utils.Config
import com.dazone.crewphoto.utils.Constants

@Database(
    entities = [User::class],
    version = Config.DB_VERSION,
    exportSchema = false
)
abstract class DazoneDatabase: RoomDatabase() {
    abstract fun getUserDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: DazoneDatabase?= null
        fun getDatabase(context: Context): DazoneDatabase {
            if(INSTANCE != null) {
                return INSTANCE!!
            }

            synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    DazoneDatabase::class.java,
                    Constants.DAZONE_DATABASE_NAME
                ).build()
            }
        }
    }
}