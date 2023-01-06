package com.b18dccn562.phonemanager.di

import android.app.Application
import com.b18dccn562.phonemanager.local_database.room.dao.AppDao
import com.b18dccn562.phonemanager.local_database.room.database.AppDatabase
import com.b18dccn562.phonemanager.local_database.room.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun providesAppDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.appDao()
    }

    @Provides
    @Singleton
    fun providesAppRepository(appDao: AppDao): AppRepository {
        return AppRepository(appDao)
    }

}