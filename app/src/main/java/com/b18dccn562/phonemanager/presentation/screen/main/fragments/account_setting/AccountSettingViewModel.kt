package com.b18dccn562.phonemanager.presentation.screen.main.fragments.account_setting

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.AccountRepository
import com.b18dccn562.phonemanager.utils.RealPathUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val application: Application,
    private val accountPreference: AccountPreference,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    //    private val _user = MutableLiveData<UserDTO>()
//    val user: LiveData<UserDTO> = _user
//
//    private var newUser: UserDTO? = null
//    private val _updateCompleted = MutableLiveData(false)
//    val updateCompleted: LiveData<Boolean> = _updateCompleted
//
//    private var imageUri: Uri? = null
//
//    private var password: String? = null
//
//    private var numberOfPropertyChange = 0
//
//    fun setUser(data: UserDTO) {
//        _user.postValue(data)
//        password = data.password
//    }
//
//    fun setImage(uri: Uri) {
//        imageUri = uri
//        numberOfPropertyChange++
//    }
//
//    fun updateUserProfile(username: String = "") {
//        if (username != user.value!!.username && username.isNotEmpty()) {
//            numberOfPropertyChange++
//        }
//        if (imageUri != null) {
//            val file = File(RealPathUtils.getRealPath(application, imageUri))
//            val body = MultipartBody.Part.createFormData(
//                "image",
//                file.name,
//                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//            )
//            userService.changeAvatar(
//                body,
//                user.value!!.email,
//                user.value!!.accessToken
//            ).enqueue(object : Callback<BaseResponse<UserDTO>> {
//                override fun onResponse(
//                    call: Call<BaseResponse<UserDTO>>,
//                    response: Response<BaseResponse<UserDTO>>
//                ) {
//                    numberOfPropertyChange--
//                }
//
//                override fun onFailure(call: Call<BaseResponse<UserDTO>>, t: Throwable) {
//                    numberOfPropertyChange--
//                }
//
//            })
//        }
//        if (username != user.value!!.username && username.isNotEmpty()) {
//            userService.changeUsername(
//                user.value!!.email,
//                user.value!!.accessToken,
//                username
//            ).enqueue(object : Callback<BaseResponse<UserDTO>> {
//                override fun onResponse(
//                    call: Call<BaseResponse<UserDTO>>,
//                    response: Response<BaseResponse<UserDTO>>
//                ) {
//                    accountUtils.updateUsername(response.body()!!.data!!.username)
//                    numberOfPropertyChange--
//                    if (numberOfPropertyChange == 0) {
//                        onUpdateComplete()
//                    }
//                }
//
//                override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
//                    numberOfPropertyChange--
//                    if (numberOfPropertyChange == 0) {
//                        onUpdateComplete()
//                    }
//                }
//
//            })
//        }
//        if (password != user.value!!.password && password!!.isNotEmpty()) {
//            userService.changePassword(
//                user.value!!.email,
//                user.value!!.accessToken,
//                password!!
//            ).enqueue(object : Callback<BaseResponse<User>> {
//                override fun onResponse(
//                    call: Call<BaseResponse<User>>,
//                    response: Response<BaseResponse<User>>
//                ) {
//                    accountUtils.updatePassword(response.body()!!.data!!.password)
//                    numberOfPropertyChange--
//                    if (numberOfPropertyChange == 0) {
//                        onUpdateComplete()
//                    }
//                }
//
//                override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
//                    numberOfPropertyChange--
//                    if (numberOfPropertyChange == 0) {
//                        onUpdateComplete()
//                    }
//                }
//
//            })
//        }
//    }
//
//    private fun onUpdateComplete() {
//        userService.getUser(
//            user.value!!.email,
//            user.value!!.accessToken
//        ).enqueue(object : Callback<BaseResponse<User>> {
//            override fun onResponse(
//                call: Call<BaseResponse<User>>,
//                response: Response<BaseResponse<User>>
//            ) {
//                newUser = response.body()!!.data!!
//                _updateCompleted.postValue(true)
//            }
//
//            override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
//
//            }
//
//        })
//    }
//
//    fun setPasswordToChange(newPassword: String) {
//        if (newPassword.isNotEmpty()) {
//            password = newPassword
//            numberOfPropertyChange++
//        }
//    }
//
//    fun getNewUser(): User {
//        return newUser!!
//    }
    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    private var email: String = ""

    private val _updatingState = MutableLiveData<Resource<BaseResponse<UserDTO>>>()
    val updatingState: LiveData<Resource<BaseResponse<UserDTO>>> = _updatingState

    private val _isInEditMode = MutableLiveData(false)
    val isInEditMode: LiveData<Boolean> = _isInEditMode

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> = _fullName

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    private val _dateOfBirth = MutableLiveData<String>()
    val dateOfBirth: LiveData<String> = _dateOfBirth

    fun goToEditMode() {
        _isInEditMode.value = true
    }

    fun update() {
        scope.launch {
            val image = if (_imageUri.value != null) {
                val file = File(RealPathUtils.getRealPath(application, _imageUri.value))
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            } else {
                null
            }
            val username = if (_fullName.value != null) {
                _fullName.value
            } else {
                null
            }
            val password = if (_password.value != null) {
                _password.value
            } else {
                null
            }
            _updatingState.postValue(Resource.Loading())
            try {
                val user = accountRepository.updateUserInformation(
                    email,
                    image,
                    username,
                    password
                )
                _updatingState.postValue(Resource.Success(user))
            } catch (exception: Exception) {
                _updatingState.postValue(Resource.Error(exception = exception))
            }
        }
    }

    fun setImage(image: Uri) {
        _imageUri.value = image
    }

    fun setEmail(userEmail: String) {
        email = userEmail
    }


}