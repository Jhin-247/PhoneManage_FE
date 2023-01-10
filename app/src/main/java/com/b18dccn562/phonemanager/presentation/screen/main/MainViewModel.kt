package com.b18dccn562.phonemanager.presentation.screen.main

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.AccountRepository
import com.b18dccn562.phonemanager.service.AppService
import com.b18dccn562.phonemanager.service.FirebaseService
import com.b18dccn562.phonemanager.utils.getRealtimeDatabase
import com.b18dccn562.phonemanager.utils.signUpNewAccountVariables
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val accountRepository: AccountRepository,
    private val accountPreference: AccountPreference
) : ViewModel() {

    private val database = getRealtimeDatabase()
    private var listener: ValueEventListener? = null

    private val job = Job()

    private val mainViewModelScope = CoroutineScope(Dispatchers.IO + job)

    private val _showLoadingDialogState = MutableLiveData(false)
    val showLoadingDialogState: LiveData<Boolean> = _showLoadingDialogState

    private val _loginState = MutableLiveData<Resource<BaseResponse<UserDTO>>?>()
    val loginState: LiveData<Resource<BaseResponse<UserDTO>>?> = _loginState

    private val _signupState = MutableLiveData<Resource<BaseResponse<UserDTO>>?>()
    val signupState: LiveData<Resource<BaseResponse<UserDTO>>?> = _signupState

    private val _userInformation = MutableLiveData<UserDTO>()
    val userInformation: LiveData<UserDTO> = _userInformation

    private val _showHeaderAndFooter = MutableLiveData(true)
    val showHeaderAndFooter: LiveData<Boolean> = _showHeaderAndFooter

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout

    fun setUserDto(user: UserDTO) {
        _userInformation.value = user
    }

    //getUserInfo
    fun getUserEmail(): String? {
        return _userInformation.value?.email
    }

    fun getUserRole(): Int? {
        return _userInformation.value?.role
    }

    fun getUserName(): String? {
        return _userInformation.value?.username
    }

    fun getUserDTO(): UserDTO? {
        return _userInformation.value
    }

    fun getPassword(): String? {
        return _userInformation.value?.password
    }

    fun showLoadingDialog() {
        _showLoadingDialogState.value = true
    }

    fun hideLoadingDialog() {
        _showLoadingDialogState.value = false
    }

    fun doLogin(email: String, password: String) {
        _loginState.value = Resource.Loading()
        mainViewModelScope.launch {
            try {
                val loginResponse = accountRepository.doLogin(email, password)
                _loginState.postValue(Resource.Success(loginResponse))
                if (loginResponse.code == Constants.ResponseCode.SUCCESS) {
                    AppService.username = loginResponse.data!!.username
                    accountPreference.saveAccount(email, password)
                    _userInformation.postValue(loginResponse.data)
                    setupFirebaseObserve()
                    signUpNewAccountVariables(email)
                }
            } catch (exception: Exception) {
                _loginState.postValue(Resource.Error(exception = exception))
            }
        }
    }

    fun doSignup(email: String, password: String, username: String, role: Int) {
        _signupState.value = Resource.Loading()
        mainViewModelScope.launch {
            try {
                val signupResponse = accountRepository.doSignup(email, password, username, role)
                _signupState.postValue(Resource.Success(signupResponse))
                if (signupResponse.code == Constants.ResponseCode.SUCCESS) {
                    AppService.username = signupResponse.data!!.username
                    accountPreference.saveAccount(email, password)
                    _userInformation.postValue(signupResponse.data)
                    setupFirebaseObserve()
                    signUpNewAccountVariables(email)
                }
            } catch (exception: Exception) {
                _signupState.postValue(Resource.Error(exception = exception))
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetSignupState() {
        _signupState.value = null
    }

    fun showHeaderAndFooter() {
        _showHeaderAndFooter.value = true
    }

    fun hideHeaderAndFooter() {
        _showHeaderAndFooter.value = false
    }

    fun logout() {
        removeOldListener(getUserEmail()!!)
        accountPreference.logout()
        _logout.value = true
        AppService.username = ""
    }

    private fun setupFirebaseObserve() {
        observeRealtimeDatabaseFirebaseNotification(getUserEmail()!!)
    }

    private fun observeRealtimeDatabaseFirebaseNotification(email: String) {
        val ref = email.replace(".", "_")
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    handleNotification()
                    clearFirebaseNotification()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.getReference(ref).child(Constants.FirebaseReference.HAS_NOTIFICATION)
            .addValueEventListener(listener!!)
    }

    private fun handleNotification() {
        val serviceIntent = Intent(application, FirebaseService::class.java)
        application.startService(serviceIntent)
    }

    private fun clearFirebaseNotification() {
        val ref = getUserEmail()!!.replace(".", "_")
        database.getReference(ref).child(Constants.FirebaseReference.HAS_NOTIFICATION)
            .setValue(false)
    }

    private fun removeOldListener(email: String) {
        val ref = email.replace(".", "_")
        listener?.let {
            database.getReference(ref).child(Constants.FirebaseReference.HAS_NOTIFICATION)
                .removeEventListener(it)
            listener = null
        }
    }

}