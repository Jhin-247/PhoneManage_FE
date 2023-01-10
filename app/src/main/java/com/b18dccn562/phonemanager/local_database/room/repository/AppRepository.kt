package com.b18dccn562.phonemanager.local_database.room.repository

import androidx.lifecycle.LiveData
import com.b18dccn562.phonemanager.local_database.room.dao.AppDao
import com.b18dccn562.phonemanager.local_database.room.local_model.AppSetting
import com.b18dccn562.phonemanager.local_database.room.local_model.DailyUsage
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.local_database.room.local_model.Setting

class AppRepository(private val appDao: AppDao) {

    val allSetting: LiveData<List<Setting>> = appDao.readAppSettings()

    suspend fun addApp(app: ItemApp) {
        appDao.insertApp(app)
    }

    suspend fun addSetting(setting: Setting) {
        val appSetting = appDao.getAppSetting1(setting.appPackageName)
        if (appSetting.isEmpty()) {
            appDao.insertSetting(setting)
        }

//        appDao.insertSetting(setting)
    }

    suspend fun getAllApps(): List<ItemApp> {
        return appDao.getAllApps()
    }

    suspend fun getAppSetting(packageName: String): List<AppSetting> {
        return appDao.getAppSetting(packageName)
    }

    suspend fun lockOrUnLockApp(item: ItemApp) {
        val appSetting = getAppSetting(item.packageName)[0].setting
        appSetting.isLock = !appSetting.isLock
        updateSetting(appSetting)
    }

    private suspend fun updateSetting(item: Setting){
        appDao.updateSetting(item)
    }

    suspend fun changeAppLimit(item: ItemApp, timeLimit: Long) {
        val appSetting = getAppSetting(item.packageName)[0].setting
        appSetting.isLimited = timeLimit
        updateSetting(appSetting)
    }

    suspend fun getAllAppLimit(): List<ItemApp> {
        val allAppSetting = appDao.getAllAppSetting()
        val result = mutableListOf<ItemApp>()
        for (app in allAppSetting) {
            if (app.isLimited > 0) {
                result.add(appDao.getAppFromPackageName(app.appPackageName))
            }
        }
        return result
    }

    suspend fun getAllAppLock(): List<ItemApp> {
        val allAppSetting = appDao.getAllAppSetting()
        val result = mutableListOf<ItemApp>()
        for (app in allAppSetting) {
            if (app.isLock) {
                result.add(appDao.getAppFromPackageName(app.appPackageName))
            }
        }
        return result
    }

    suspend fun getAppUsageInDay(packageName: String): Long {
        return appDao.getUsage(packageName).timeUsedInDay
    }

    suspend fun getAppUsage(packageName: String): DailyUsage {
        return appDao.getUsage(packageName)
    }

    suspend fun insertAppUsage(usage: DailyUsage) {
        appDao.insertDailyUsage(usage)
    }

    suspend fun getAllAppUsage(): List<DailyUsage> {
        return appDao.getAllAppUsage()
    }

    suspend fun getApp(packageName: String): ItemApp {
        return appDao.getAppFromPackageName(packageName)
    }

    suspend fun getAllAppSetting(): List<Setting> {
        return appDao.getAllAppSettings()
    }
}