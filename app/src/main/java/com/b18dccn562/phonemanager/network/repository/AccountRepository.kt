package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.services.AccountService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountService: AccountService
) {

    suspend fun doLogin(email: String, password: String): BaseResponse<UserDTO> {
        return accountService.signInAsync(email, password).await()
    }

    suspend fun doSignup(
        email: String,
        password: String,
        username: String,
        role: Int
    ): BaseResponse<UserDTO> {
        return accountService.signupAccountAsync(email, password, username, role).await()
    }

    suspend fun changeAvatar(image: MultipartBody.Part, email: String): BaseResponse<String> {
        return accountService.changeAvatarAsync(image, email, "").await()
    }

    suspend fun updateUserInformation(
        userEmail: String,
        image: MultipartBody.Part?,
        username: String?,
        password: String?
    ): BaseResponse<UserDTO> {
        return accountService.updateUserInformationAsync(userEmail, image, username, password)
            .await()
    }

    suspend fun createSubAccount(
        myEmail: String,
        email: String,
        password: String,
        username: String,
        role: Int
    ): BaseResponse<UserDTO> {
        return accountService.createSubAccountAsync(
            myEmail,
            email,
            password,
            username,
            role
        ).await()
    }

    suspend fun getPartnerInfo(
        myEmail: String
    ): BaseResponse<UserDTO> {
        return accountService.getPartnerInfoAsync(myEmail).await()
    }

    suspend fun getChildrenInfo(
        myEmail: String
    ): BaseResponse<List<UserDTO>> {
        return accountService.getChildrenInfoAsync(myEmail).await()
    }

    suspend fun searchChildren(
        search: String,
        email: String
    ): BaseResponse<List<UserDTO>> {
        return accountService.searchChildrenAsync(search, email).await()
    }

    suspend fun searchParent(
        search: String,
        email: String
    ): BaseResponse<List<UserDTO>> {
        return accountService.searchPartnerAsync(search, email).await()
    }

    suspend fun addPartner(
        requesterEmail: String,
        receiverEmail: String,
        requestTime: Long,
        action: Int
    ): BaseResponse<RequestDTO> {
        return accountService.addPartnerAsync(
            requesterEmail,
            receiverEmail,
            requestTime,
            action
        ).await()
    }

    suspend fun addChildren(
        requesterEmail: String,
        receiverEmail: String,
        requestTime: Long,
        action: Int
    ): BaseResponse<RequestDTO> {
        return accountService.addChildrenAsync(
            requesterEmail,
            receiverEmail,
            requestTime,
            action
        ).await()
    }

}