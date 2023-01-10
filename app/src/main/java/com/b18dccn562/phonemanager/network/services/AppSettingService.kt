package com.b18dccn562.phonemanager.network.services

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.dto.AppSettingDTO
import com.b18dccn562.phonemanager.network.dto.ClassRequestDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppSettingService {

    @GET(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.GET_CLASS_REQUEST)
    fun loadRequestForClassAsync(
        @Query("class_id") classId: Long
    ): Deferred<BaseResponse<List<ClassRequestDTO>>>

    @GET(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.GET_USER_APP_SETTING)
    fun loadAppSettingsAsync(
        @Query("get_user_setting") userId: Long
    ): Deferred<BaseResponse<List<AppSettingDTO>>>

    @POST(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.UPLOAD_USER_APP_SETTINGS)
    fun uploadAppSettingsAsync(
        @Query("user_id") userId: Long,
        @Query("app_setting") appSetting: List<AppSettingDTO>
    ): Deferred<BaseResponse<List<AppSettingDTO>>>

    @POST(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.UPLOAD_USER_APP_SETTING)
    fun uploadAppSettingAsync(
        @Query("user_id") userId: Long,
        @Query("app_package") appPackageName: String,
        @Query("is_lock") isLock: Boolean,
        @Query("is_limited") isLimited: Long
    ): Deferred<BaseResponse<AppSettingDTO>>

    @POST(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.UPLOAD_APP_FOR_DATABASE_CHECK)
    fun uploadAppForDatabaseCheckAsync(
        @Query("apps") appPackages: List<String>
    ): Deferred<BaseResponse<List<String>>>
}