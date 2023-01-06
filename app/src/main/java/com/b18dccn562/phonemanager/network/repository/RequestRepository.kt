package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import com.b18dccn562.phonemanager.network.services.RequestService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepository @Inject constructor(
    private val requestService: RequestService
) {
    suspend fun getRequest(userEmail: String): BaseResponse<List<RequestDTO>> {
        return requestService.getRequestAsync(userEmail).await()
    }

    suspend fun acceptRequest(requestDTO: RequestDTO): BaseResponse<List<RequestDTO>> {
        return requestService.acceptRequestAsync(requestDTO.id).await()
    }

    suspend fun denyRequest(requestDTO: RequestDTO): BaseResponse<List<RequestDTO>> {
        return requestService.denyRequestAsync(requestDTO.id).await()
    }
}