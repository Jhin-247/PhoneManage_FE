package com.b18dccn562.phonemanager.network.services

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.dto.NotificationDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {

    @GET(Constants.ApiReferences.NOTIFICATION_REFERENCE + Constants.ApiReferences.GET_NOTIFICATION)
    fun getNotificationsAsync(
        @Query("user_email") email: String
    ): Deferred<BaseResponse<List<NotificationDTO>>>

}