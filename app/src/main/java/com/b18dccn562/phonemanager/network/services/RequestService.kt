package com.b18dccn562.phonemanager.network.services

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RequestService {

    @GET(Constants.ApiReferences.REQUEST_REFERENCE + Constants.ApiReferences.GET_MY_REQUEST)
    fun getRequestAsync(
        @Query("user_email") userEmail: String
    ): Deferred<BaseResponse<List<RequestDTO>>>

    @POST(Constants.ApiReferences.REQUEST_REFERENCE + Constants.ApiReferences.ACCEPT_REQUEST)
    fun acceptRequestAsync(
        @Query("request_id") requestId: Long
    ): Deferred<BaseResponse<List<RequestDTO>>>

    @POST(Constants.ApiReferences.REQUEST_REFERENCE + Constants.ApiReferences.DENY_REQUEST)
    fun denyRequestAsync(
        @Query("request_id") requestId: Long
    ): Deferred<BaseResponse<List<RequestDTO>>>

}