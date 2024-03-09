package com.example.ezsail.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ezsail.Constants.SAILING_DATABASE_NAME
import com.example.ezsail.db.SailingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton
import javax.security.auth.callback.Callback

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Ensure only one instance of database
    @Provides
    // Tell dagger the result of this function can be used to create other dependencies or inject to classes
    fun provideSailingDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            SailingDatabase::class.java,
            SAILING_DATABASE_NAME
    ).createFromAsset("database/PYNumbers.db").build() // Prepopulate database with PY numbers file

    @Singleton
    @Provides
    // dagger will automatically figure out how to construct the db instance here
    // as the manual provided above
    fun provideSailingDao(db: SailingDatabase) = db.getSailingDao()
}