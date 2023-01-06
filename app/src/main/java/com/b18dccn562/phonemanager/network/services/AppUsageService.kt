package com.b18dccn562.phonemanager.network.services

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.dto.AppUsageDTO
import com.b18dccn562.phonemanager.network.dto.ReportDTO
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface AppUsageService {

    @POST(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.UPLOAD_APP_USAGE)
    fun uploadAppUsageAsync(
        @Query("email") email: String,
        @Query("app_package") appPackage: String,
        @Query("app_name") appName: String,
        @Query("time") time: Long,
        @Query("action") action: Int
    ): Deferred<BaseResponse<Boolean>>

    @Multipart
    @POST(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.UPLOAD_APP)
    fun uploadAppAsync(
        @Part("app_package") appPackage: String,
        @Part("app_name") appName: String,
        @Part image: MultipartBody.Part
    ): Deferred<BaseResponse<Boolean>>

    @GET(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.GET_APP_USAGE)
    fun getUserAppUsageAsync(
        @Query("email") email: String,
        @Query("type_query") typeRequest: Int
    ): Deferred<BaseResponse<List<AppUsageDTO>>>

    @POST(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.UPLOAD_APP_USAGE_VIOLATION)
    fun uploadAppUsageViolationAsync(
        @Query("email") email: String,
        @Query("app_package") appPackage: String,
        @Query("app_name") appName: String,
        @Query("time") time: Long,
        @Query("action") action: Int,
        @Query("class_id") classId: Long
    ): Deferred<BaseResponse<Boolean>>

    @GET(Constants.ApiReferences.APP_REFERENCE + Constants.ApiReferences.GET_VIOLATION)
    fun getClassReportAsync(
        @Query("class_id") classId: Long
    ): Deferred<BaseResponse<List<ReportDTO>>>

}