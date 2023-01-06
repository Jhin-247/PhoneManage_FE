package com.b18dccn562.phonemanager.network.services

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface AccountService {

    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.LOGIN)
    fun signInAsync(
        @Query("email") email: String,
        @Query("password") password: String
    ): Deferred<BaseResponse<UserDTO>>

    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.CREATE_ACCOUNT)
    fun signupAccountAsync(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("username") username: String,
        @Query("role") role: Int
    ): Deferred<BaseResponse<UserDTO>>


    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.CREATE_ACCOUNT)
    fun createAccountAsync(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("username") username: String,
        @Query("role") role: Int
    ): Deferred<BaseResponse<UserDTO>>

    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.CREATE_SUB_ACCOUNT)
    fun createSubAccountAsync(
        @Query("super_user") myEmail: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("username") username: String,
        @Query("role") role: Int
    ): Deferred<BaseResponse<UserDTO>>

    @Multipart
    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.UPDATE_USER_INFORMATION)
    fun updateUserInformationAsync(
        @Part("sender") email: String,
        @Part image: MultipartBody.Part?,
        @Part("username") username: String?,
        @Part("password") password: String?
    ): Deferred<BaseResponse<UserDTO>>

    @Multipart
    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.CHANGE_AVATAR)
    fun changeAvatarAsync(
        @Part image: MultipartBody.Part,
        @Part("email") email: String,
        @Part("access_token") accessToken: String
    ): Deferred<BaseResponse<String>>

    @GET(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.GET_PARTNER_INFO)
    fun getPartnerInfoAsync(
        @Query("email") email: String
    ): Deferred<BaseResponse<UserDTO>>

    @GET(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.GET_CHILDREN_INFO)
    fun getChildrenInfoAsync(
        @Query("email") email: String
    ): Deferred<BaseResponse<List<UserDTO>>>

    @GET(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.SEARCH_CHILDREN)
    fun searchChildrenAsync(
        @Query("search_children") search: String,
        @Query("email") email: String
    ): Deferred<BaseResponse<List<UserDTO>>>

    @GET(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.SEARCH_PARTNER)
    fun searchPartnerAsync(
        @Query("search_partner") search: String,
        @Query("email") email: String
    ): Deferred<BaseResponse<List<UserDTO>>>

    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.ADD_PARTNER)
    fun addPartnerAsync(
        @Query("requester_id") requesterEmail: String,
        @Query("receiver_id") receiverEmail: String,
        @Query("request_time") requestTime: Long,
        @Query("action") action: Int
    ): Deferred<BaseResponse<RequestDTO>>

    @POST(Constants.ApiReferences.USER_REFERENCE + Constants.ApiReferences.ADD_CHILDREN)
    fun addChildrenAsync(
        @Query("requester_id") requesterEmail: String,
        @Query("receiver_id") receiverEmail: String,
        @Query("request_time") requestTime: Long,
        @Query("action") action: Int
    ): Deferred<BaseResponse<RequestDTO>>
}