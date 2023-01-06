package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.AppUsageDTO
import com.b18dccn562.phonemanager.network.dto.ReportDTO
import com.b18dccn562.phonemanager.network.services.AppUsageService
import okhttp3.MultipartBody
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageRepository @Inject constructor(
    private val appUsageService: AppUsageService
) {
    suspend fun uploadAppUsage(
        @Query("email") email: String,
        @Query("app_package") appPackage: String,
        @Query("app_name") appName: String,
        @Query("time") time: Long,
        @Query("action") action: Int
    ): BaseResponse<Boolean> {
        return appUsageService.uploadAppUsageAsync(email, appPackage, appName, time, action).await()
    }

    suspend fun uploadAppImage(
        appPackage: String,
        appName: String,
        image: MultipartBody.Part
    ): BaseResponse<Boolean> {
        return appUsageService.uploadAppAsync(appPackage, appName, image).await()
    }

    suspend fun getUserAppUsage(
        userEmail: String,
        queryType: Int
    ): BaseResponse<List<AppUsageDTO>> {
        return appUsageService.getUserAppUsageAsync(userEmail, queryType).await()
    }

    suspend fun uploadAppUsageViolation(
        email: String,
        packageName: String,
        appName: String,
        currentTimeMillis: Long,
        appAction: Int,
        classId: Long
    ): BaseResponse<Boolean> {
        return appUsageService.uploadAppUsageViolationAsync(
            email,
            packageName,
            appName,
            currentTimeMillis,
            appAction,
            classId
        ).await()
    }

    suspend fun getClassReport(classId: Long): BaseResponse<List<ReportDTO>> {
        return appUsageService.getClassReportAsync(
            classId
        ).await()
    }

}