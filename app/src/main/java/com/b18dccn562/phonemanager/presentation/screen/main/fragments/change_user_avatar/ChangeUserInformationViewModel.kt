package com.b18dccn562.phonemanager.presentation.screen.main.fragments.change_user_avatar

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChangeUserInformationViewModel @Inject constructor(
//    private val userService: UserService,
//    private val application: Application
) : ViewModel() {
//    var userAvatarUri: Uri? = null
//    private var userInformation: User? = null
//    private val _uploadComplete = MutableLiveData(false)
//    val uploadComplete: LiveData<Boolean> = _uploadComplete
//
//    private val _newUser = MutableLiveData<BaseResponse<User>>()
//    val userUpdateState: LiveData<BaseResponse<User>> = _newUser
//
//    fun changeUserAvatar() {
//        if (userInformation != null && userAvatarUri != null) {
//            val file = File(RealPathUtils.getRealPath(application, userAvatarUri))
//            val body = MultipartBody.Part.createFormData(
//                "image",
//                file.name,
//                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//            )
//            userService.changeAvatar(
//                body,
//                userInformation!!.email,
//                userInformation!!.accessToken
//            ).enqueue(object : Callback<BaseResponse<User>> {
//                override fun onResponse(
//                    call: Call<BaseResponse<User>>,
//                    response: Response<BaseResponse<User>>
//                ) {
//                    processResult(response)
//                }
//
//                override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
//                    Log.d("OkHttp","${t.message}")
//                }
//
//            })
//        }
//    }
//
//    private fun processResult(response: Response<BaseResponse<User>>) {
//        _newUser.value = response.body()
//        _uploadComplete.value = true
//    }
//
//    fun setUserValue(value: User?) {
//        userInformation = value
//    }

}