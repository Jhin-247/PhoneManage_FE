package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.AppSettingDTO
import com.b18dccn562.phonemanager.network.services.AppSettingService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingRepository @Inject constructor(
    private val appSettingService: AppSettingService
) {

    suspend fun loadAppSettings(userId: Long): BaseResponse<List<AppSettingDTO>> {
        return appSettingService.loadAppSettingsAsync(userId).await()
    }

    suspend fun uploadAppSettings(
        userId: Long,
        appSettings: List<AppSettingDTO>
    ): BaseResponse<List<AppSettingDTO>> {
        return appSettingService.uploadAppSettingsAsync(userId, appSettings).await()
    }

    suspend fun uploadAppSetting(
        userId: Long,
        appSetting: AppSettingDTO
    ): BaseResponse<AppSettingDTO> {
        return appSettingService.uploadAppSettingAsync(
            userId,
            appSetting.app!!.packageName,
            appSetting.isLock,
            appSetting.isLimited
        ).await()
    }

    suspend fun uploadAppForCheckingDatabase(
        apps: List<String>
    ): BaseResponse<List<String>> {
        return appSettingService.uploadAppForDatabaseCheckAsync(apps).await()
    }

}