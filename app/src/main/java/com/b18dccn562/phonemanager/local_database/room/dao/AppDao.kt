package com.b18dccn562.phonemanager.local_database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.b18dccn562.phonemanager.local_database.room.local_model.AppSetting
import com.b18dccn562.phonemanager.local_database.room.local_model.DailyUsage
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.local_database.room.local_model.Setting

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: ItemApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: Setting)

    @Query("Select * from app_setting where appPackageName =:packageName")
    suspend fun getAppSetting1(packageName: String): List<Setting>

    @Transaction
    @Query("SELECT * FROM app where packageName =:packageName")
    suspend fun getAppSetting(packageName: String): List<AppSetting>

    @Query("SELECT * FROM app_setting")
    fun readAppSettings(): LiveData<List<Setting>>

    @Query("SELECT * FROM app")
    suspend fun getAllApps(): List<ItemApp>

    @Query("SELECT * FROM app_setting")
    suspend fun getAllAppSetting(): List<Setting>

    @Query("SELECT * FROM app where packageName =:packageName")
    suspend fun getAppFromPackageName(packageName: String): ItemApp

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyUsage(dailyUsage: DailyUsage)

    @Query("SELECT * FROM app_daily_usage where packageName =:packageName")
    suspend fun getUsage(packageName: String): DailyUsage

    @Query("SELECT * FROM app_daily_usage")
    suspend fun getAllAppUsage(): List<DailyUsage>

    @Query("SELECT * FROM app_setting")
    suspend fun getAllAppSettings(): List<Setting>

    @Update
    suspend fun updateSetting(item: Setting)
}