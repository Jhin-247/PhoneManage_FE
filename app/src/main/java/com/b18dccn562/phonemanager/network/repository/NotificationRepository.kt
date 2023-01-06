package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.NotificationDTO
import com.b18dccn562.phonemanager.network.services.NotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationService: NotificationService
) {

    suspend fun getNotifications(userEmail: String): BaseResponse<List<NotificationDTO>> {
        return notificationService.getNotificationsAsync(userEmail).await()
    }

}